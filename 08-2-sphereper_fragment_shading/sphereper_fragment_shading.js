// 201835526 정현우
"use strict";

let canvas;
let gl;

let numTimesToSubdivide = 4;

let index = 0;
let pointsArray = [];
let normalArray = []

let near = -10;
let far = 10;
let radius = 1.5;
let theta = 0.0;
let phi = 0.0;
let dr = 5.0 * Math.PI / 180.0;

let left = -3.0;
let right = 3.0;
let ytop = 3.0;
let bottom = -3.0;

let va = vec4(0.0, 0.0, -1.0, 1);
let vb = vec4(0.0, 0.942809, 0.333333, 1);
let vc = vec4(-0.816497, -0.471405, 0.333333, 1);
let vd = vec4(0.816497, -0.471405, 0.333333, 1);

let lightPosition = vec4(10.0, 10.0, 10.0, 0.0)
let lightAmbient = vec4(0.2, 0.2, 0.2, 1.0)
let lightDiffuse = vec4(1.0, 1.0, 1.0, 1.0)
let lightSpecular = vec4(1.0, 1.0, 1.0, 1.0)

let materialAmbient = vec4(1.0, 0.0, 1.0, 1.0)
let materialDiffuse = vec4(1.0, 0.8, 0.0, 1.0)
let materialSpecular = vec4(1.0, 0.8, 0.0, 1.0)

let materialShininess = 2.0

let ctm
let ambientColor, diffuseColor, specularColor


let modelViewMatrix, projectionMatrix;
let modelViewMatrixLoc, projectionMatrixLoc;


let eye;
let at = vec3(0.0, 0.0, 0.0);
let up = vec3(0.0, 1.0, 0.0);

function triangle(a, b, c) {

    pointsArray.push(a);
    pointsArray.push(b);
    pointsArray.push(c);

    normalArray.push(a[0], a[1], a[2], 0.0)
    normalArray.push(b[0], b[1], b[2], 0.0)
    normalArray.push(c[0], c[1], c[2], 0.0)

    index += 3;
}

function divideTriangle(a, b, c, count) {
    if (count > 0) {

        let ab = normalize(mix(a, b, 0.5), true);
        let ac = normalize(mix(a, c, 0.5), true);
        let bc = normalize(mix(b, c, 0.5), true);

        divideTriangle(a, ab, ac, count - 1);
        divideTriangle(ab, b, bc, count - 1);
        divideTriangle(bc, c, ac, count - 1);
        divideTriangle(ab, bc, ac, count - 1);
    } else {
        triangle(a, b, c);
    }
}

function tetrahedron(a, b, c, d, n) {
    divideTriangle(a, b, c, n);
    divideTriangle(d, c, b, n);
    divideTriangle(a, d, b, n);
    divideTriangle(a, c, d, n);
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
    gl.enable(gl.CULL_FACE);
    gl.cullFace(gl.FRONT);

    let program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);


    let ambientProduct = mult(lightAmbient, materialAmbient)
    let diffuseProduct = mult(lightDiffuse, materialDiffuse)
    let specularProduct = mult(lightSpecular, materialSpecular)

    tetrahedron(va, vb, vc, vd, numTimesToSubdivide);

    let nBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, nBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(normalArray), gl.STATIC_DRAW);

    let vNormal = gl.getAttribLocation(program, "vNormal");
    gl.vertexAttribPointer(vNormal, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vNormal);

    let vBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(pointsArray), gl.STATIC_DRAW);

    let vPosition = gl.getAttribLocation(program, "vPosition");
    gl.vertexAttribPointer(vPosition, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);

    modelViewMatrixLoc = gl.getUniformLocation(program, "modelViewMatrix");
    projectionMatrixLoc = gl.getUniformLocation(program, "projectionMatrix");

    document.getElementById("Button0").onclick = function () {
        radius *= 2.0;
    };
    document.getElementById("Button1").onclick = function () {
        radius *= 0.5;
    };
    document.getElementById("Button2").onclick = function () {
        phi += dr;
    };
    document.getElementById("Button3").onclick = function () {
        phi -= dr;
    };
    document.getElementById("Button4").onclick = function () {
        numTimesToSubdivide++;
        index = 0;
        pointsArray = [];
        normalArray = []
        init();
    };
    document.getElementById("Button5").onclick = function () {
        if (numTimesToSubdivide) numTimesToSubdivide--;
        index = 0;
        pointsArray = [];
        normalArray = []
        init();
    };

    gl.uniform4fv(gl.getUniformLocation(program, "ambientProduct"), flatten(ambientProduct))
    gl.uniform4fv(gl.getUniformLocation(program, "diffuseProduct"), flatten(diffuseProduct))
    gl.uniform4fv(gl.getUniformLocation(program, "specularProduct"), flatten(specularProduct))
    gl.uniform4fv(gl.getUniformLocation(program, "lightPosition"), flatten(lightPosition))
    gl.uniform1f(gl.getUniformLocation(program, "shininess"), materialShininess)

    render();
}

function render() {

    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

    eye = vec3(radius * Math.cos(theta) * Math.sin(phi), radius * Math.sin(theta),
        radius * Math.cos(theta) * Math.cos(phi)); // eye point

    modelViewMatrix = lookAt(eye, at, up);
    projectionMatrix = ortho(left, right, bottom, ytop, near, far);

    gl.uniformMatrix4fv(modelViewMatrixLoc, false, flatten(modelViewMatrix));
    gl.uniformMatrix4fv(projectionMatrixLoc, false, flatten(projectionMatrix));

    for (let i = 0; i < index; i += 3)
        gl.drawArrays(gl.TRIANGLES, i, 3);

    window.requestAnimFrame(render);
}
