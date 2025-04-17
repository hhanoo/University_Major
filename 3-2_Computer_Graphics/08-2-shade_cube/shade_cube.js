// 201835526 정현우
let canvas;
let gl;
let program

let NumVertices = 36;

let pointsArray = []
let normalArray = []

let vertices = [
    vec4(-0.5, -0.5, 0.5, 1.0), // 0
    vec4(-0.5, 0.5, 0.5, 1.0), // 1
    vec4(0.5, 0.5, 0.5, 1.0), // 2
    vec4(0.5, -0.5, 0.5, 1.0), // 3
    vec4(-0.5, -0.5, -0.5, 1.0), // 4
    vec4(-0.5, 0.5, -0.5, 1.0), // 5
    vec4(0.5, 0.5, -0.5, 1.0), // 6
    vec4(0.5, -0.5, -0.5, 1.0),  // 7
];

let lightPosition = vec4(1.0, 1.0, 1.0, 0.0)
let lightAmbient = vec4(0.2, 0.2, 0.2, 1.0)
let lightDiffuse = vec4(1.0, 1.0, 1.0, 1.0)
let lightSpecular = vec4(1.0, 1.0, 1.0, 1.0)

let materialAmbient = vec4(1.0, 0.0, 1.0, 1.0)
let materialDiffuse = vec4(1.0, 0.8, 0.0, 1.0)
let materialSpecular = vec4(1.0, 0.8, 0.0, 1.0)

let materialshininess = 100.0

let ctm;
let ambientColor, diffuseColor, specularColor

let modelViewMatrix
let projectionMatrix

let viewerPos

let xAxis = 0
let yAxis = 1
let zAxis = 2
let axis = 0

let theta = [0, 0, 0]
let thetaLoc
let flag = true

function colorCube() {
    quad(1, 0, 3, 2);
    quad(2, 3, 7, 6);
    quad(3, 0, 4, 7);
    quad(6, 5, 1, 2);
    quad(4, 5, 6, 7);
    quad(5, 4, 0, 1);
}

function quad(a, b, c, d) {

    let t1 = subtract(vertices[b], vertices[a])
    let t2 = subtract(vertices[c], vertices[a])
    let normal = cross(t1, t2)
    normal = vec3(normal)

    pointsArray.push(vertices[a])
    normalArray.push(normal)
    pointsArray.push(vertices[b])
    normalArray.push(normal)
    pointsArray.push(vertices[c])
    normalArray.push(normal)
    pointsArray.push(vertices[a])
    normalArray.push(normal)
    pointsArray.push(vertices[c])
    normalArray.push(normal)
    pointsArray.push(vertices[d])
    normalArray.push(normal)

}

window.onload = function init() {
    canvas = document.getElementById("gl-canvas");

    gl = WebGLUtils.setupWebGL(canvas);
    if (!gl) {
        alert("WebGL isn't available");
    }

    gl.viewport(0, 0, canvas.width, canvas.height);
    gl.clearColor(1.0, 1.0, 1.0, 1.0);
    gl.enable(gl.DEPTH_TEST);

    //  Load shaders and initialize attribute buffers
    program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);

    colorCube();


    let nBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, nBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(normalArray), gl.STATIC_DRAW);

    let vNormal = gl.getAttribLocation(program, "vNormal");
    gl.vertexAttribPointer(vNormal, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vNormal);


    let vBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(pointsArray), gl.STATIC_DRAW);

    let vPosition = gl.getAttribLocation(program, "vPosition");
    gl.vertexAttribPointer(vPosition, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);


    thetaLoc = gl.getUniformLocation(program, "theta")

    projectionMatrix = ortho(-1, 1, -1, 1, -100, 100)

    let ambientProduct = mult(lightAmbient, materialAmbient)
    let diffuseProduct = mult(lightDiffuse, materialDiffuse)
    let specularProduct = mult(lightSpecular, materialSpecular)

    document.getElementById("ButtonX").onclick = () => {
        axis = xAxis;
        render();
    }
    document.getElementById("ButtonY").onclick = () => {
        axis = yAxis;
        render();
    }
    document.getElementById("ButtonZ").onclick = () => {
        axis = zAxis;
        render();
    }
    document.getElementById("ButtonT").onclick = () => {
        flag = !flag;
        render();
    }

    gl.uniform4fv(gl.getUniformLocation(program, "ambientProduct"), flatten(ambientProduct))
    gl.uniform4fv(gl.getUniformLocation(program, "diffuseProduct"), flatten(diffuseProduct))
    gl.uniform4fv(gl.getUniformLocation(program, "specularProduct"), flatten(specularProduct))
    gl.uniform4fv(gl.getUniformLocation(program, "lightPosition"), flatten(lightPosition))

    gl.uniform1f(gl.getUniformLocation(program, "shininess"), materialshininess)

    gl.uniformMatrix4fv(gl.getUniformLocation(program, "projectionMatrix"), false, flatten(projectionMatrix))


    render();

}

function render() {
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

    if (flag) theta[axis] += 2.0

    modelViewMatrix = mat4()
    modelViewMatrix = mult(modelViewMatrix, rotate(theta[xAxis], [1, 0, 0]))
    modelViewMatrix = mult(modelViewMatrix, rotate(theta[yAxis], [0, 1, 0]))
    modelViewMatrix = mult(modelViewMatrix, rotate(theta[zAxis], [0, 0, 1]))

    gl.uniformMatrix4fv(gl.getUniformLocation(program, "modelViewMatrix"), false, flatten(modelViewMatrix))
    gl.drawArrays(gl.TRIANGLES, 0, NumVertices);
}