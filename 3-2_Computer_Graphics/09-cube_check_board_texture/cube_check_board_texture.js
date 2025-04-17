// 201835526 정현우
"use strict";

let canvas;
let gl;
let program;

let NumVertices = 36;
let texSize = 64;

let image1 = [];
let image2 = new Uint8Array(4 * texSize * texSize);

function createCheckboardPattern() {
    for (let i = 0; i < texSize; i++) {
        image1[i] = [];
    }
    for (let i = 0; i < texSize; i++) {
        for (let j = 0; j < texSize; j++) {
            image1[i][j] = new Float32Array(4);
        }
    }
    for (let i = 0; i < texSize; i++) {
        for (let j = 0; j < texSize; j++) {
            let c = (((i & 0x8) === 0) ^ ((j & 0x8) === 0));
            image1[i][j] = [c, c, c, 1];
        }
    }

    for (let i = 0; i < texSize; i++) {
        for (let j = 0; j < texSize; j++) {
            for (let k = 0; k < texSize; k++) {
                image2[4 * texSize * i + 4 * j + k] = 255 * image1[i][j][k];
            }
        }
    }
}

let pointsArray = [];
let colorsArray = [];
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
    let texture = gl.createTexture()
    gl.activeTexture(gl.TEXTURE0)
    gl.bindTexture(gl.TEXTURE_2D, texture)
    gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true)

    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, texSize, texSize, 0, gl.RGBA, gl.UNSIGNED_BYTE, image)

    gl.generateMipmap(gl.TEXTURE_2D)
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST_MAPMAP_LINEAR)
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST)

}

function quad(a, b, c, d) {
    pointsArray.push(vertices[a]);
    colorsArray.push(vertexColors[a]);
    texCoordsArray.push(texCoord[0]);

    pointsArray.push(vertices[b]);
    colorsArray.push(vertexColors[a]);
    texCoordsArray.push(texCoord[1]);

    pointsArray.push(vertices[c]);
    colorsArray.push(vertexColors[a]);
    texCoordsArray.push(texCoord[2]);

    pointsArray.push(vertices[a]);
    colorsArray.push(vertexColors[a]);
    texCoordsArray.push(texCoord[0]);

    pointsArray.push(vertices[c]);
    colorsArray.push(vertexColors[a]);
    texCoordsArray.push(texCoord[2]);

    pointsArray.push(vertices[d]);
    colorsArray.push(vertexColors[a]);
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
    gl.cullFace(gl.FRONT);

    program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);

    colorCube()

    let cBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, cBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(colorsArray), gl.STATIC_DRAW);

    let vColor = gl.getAttribLocation(program, "vColor");
    gl.vertexAttribPointer(vColor, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vColor);

    let vBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(pointsArray), gl.STATIC_DRAW);

    let vPosition = gl.getAttribLocation(program, "vPosition");
    gl.vertexAttribPointer(vPosition, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);

    let tBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, tBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(texCoordsArray), gl.STATIC_DRAW);

    let vTexCoord = gl.getAttribLocation(program, "vTexCoord");
    gl.vertexAttribPointer(vTexCoord, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vTexCoord);

    createCheckboardPattern()
    configureTexture(image2)

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

