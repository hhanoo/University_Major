// 201835526 정현우
"use strict";

let canvas;
let gl;
let program;

let NumVertices = 36;
let texSize = 64;

let points = [];
let colors = [];
let texCoordsArray = [];

let texture;
let texCoord = [
    vec2(0, 0),
    vec2(0, 1),
    vec2(1, 1),
    vec2(1, 0)
];

let vertices = [
    vec4(-0.5, -0.5, 0.5, 1.0), // 0
    vec4(-0.5, 0.5, 0.5, 1.0), // 1
    vec4(0.5, 0.5, 0.5, 1.0), // 2
    vec4(0.5, -0.5, 0.5, 1.0), // 3
    vec4(-0.5, -0.5, -0.5, 1.0), // 4
    vec4(-0.5, 0.5, -0.5, 1.0), // 5
    vec4(0.5, 0.5, -0.5, 1.0), // 6
    vec4(0.5, -0.5, -0.5, 1.0)  // 7
];

let vertexColors = [
    [0.0, 0.0, 0.0, 1.0],  // black
    [1.0, 0.0, 0.0, 1.0],  // red
    [1.0, 1.0, 0.0, 1.0],  // yellow
    [0.0, 1.0, 0.0, 1.0],  // green
    [0.0, 0.0, 1.0, 1.0],  // blue
    [1.0, 0.0, 1.0, 1.0],  // magenta
    [0.0, 1.0, 1.0, 1.0],  // cyan
    [1.0, 1.0, 1.0, 1.0]   // white
];

let xAxis = 0;
let yAxis = 1;
let zAxis = 2;
let axis = xAxis;
let theta = [0, 0, 0];

let modelViewMatrixLoc;

function configureTexture(image) {
    texture = gl.createTexture();
    gl.bindTexture((gl.TEXTURE_2D), texture);
    gl.pixelStorei((gl.UNPACK_FLIP_Y_WEBGL), true);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);

    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);

    gl.uniform1i(gl.getUniformLocation(program, "texture"), 0);
}

function quad(a, b, c, d) {
    points.push(vertices[a]);
    colors.push(vertexColors[a]);
    texCoordsArray.push(texCoord[0]);

    points.push(vertices[b]);
    colors.push(vertexColors[a]);
    texCoordsArray.push(texCoord[1]);

    points.push(vertices[c]);
    colors.push(vertexColors[a]);
    texCoordsArray.push(texCoord[2]);

    points.push(vertices[a]);
    colors.push(vertexColors[a]);
    texCoordsArray.push(texCoord[0]);

    points.push(vertices[c]);
    colors.push(vertexColors[a]);
    texCoordsArray.push(texCoord[2]);

    points.push(vertices[d]);
    colors.push(vertexColors[a]);
    texCoordsArray.push(texCoord[3]);
}

function colorCube() {
    quad(1, 0, 3, 2); // blue
    quad(2, 3, 7, 6); // yellow
    quad(3, 0, 4, 7); // green
    quad(6, 5, 1, 2); // cyan
    quad(4, 5, 6, 7); // red
    quad(5, 4, 0, 1); // magenta
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
    gl.enable(gl.FRONT);

    //
    //  Load shaders and initialize attribute buffers
    //
    program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);

    colorCube();

    let cBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, cBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(colors), gl.STATIC_DRAW);

    let vColor = gl.getAttribLocation(program, "vColor");
    gl.vertexAttribPointer(vColor, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vColor);

    let vBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(points), gl.STATIC_DRAW);

    let vPosition = gl.getAttribLocation(program, "vPosition");
    gl.vertexAttribPointer(vPosition, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);

    let tBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, tBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(texCoordsArray), gl.STATIC_DRAW);

    let vTexCoord = gl.getAttribLocation(program, "vTexCoord");
    gl.vertexAttribPointer(vTexCoord, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vTexCoord);

    let url = "https://c1.staticflickr.com/9/8873/18598400202_3af67ef38f_q.jpg";

    let image = new Image();
    image.onload = function () {
        configureTexture(image)
    }

    image.crossOrigin = "";
    image.src = url;

    modelViewMatrixLoc = gl.getUniformLocation(program, "modelViewMatrix");

    document.getElementById("ButtonX").onclick = function () {
        axis = xAxis;
        theta[axis] += 2.0;
    };
    document.getElementById("ButtonY").onclick = function () {
        axis = yAxis;
        theta[axis] += 2.0;
    };
    document.getElementById("ButtonZ").onclick = function () {
        axis = zAxis;
        theta[axis] += 2.0;
    };
    render();
}


function render() {
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

    let ctm = mat4();
    ctm = mult(ctm, rotate(theta[zAxis], 0, 0, 1)); // rotateZ
    ctm = mult(ctm, rotate(theta[yAxis], 0, 1, 0)); // rotateY
    ctm = mult(ctm, rotate(theta[xAxis], 1, 0, 0)); // rotateX

    gl.uniformMatrix4fv(modelViewMatrixLoc, false, flatten(ctm));
    gl.drawArrays(gl.TRIANGLES, 0, NumVertices);

    requestAnimFrame(render);
}

