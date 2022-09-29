var gl;
var points;

window.onload = function init() {
    var canvas = document.getElementById("gl-canvas");
    gl = WebGLUtils.setupWebGL(canvas);
    if (!gl) {
        alert("WebGL isn't available");
    }

    //  Configure WebGL
    gl.viewport(0, 0, canvas.width, canvas.height);
    gl.clearColor(1.0, 1.0, 1.0, 1.0);

    // Load shaders and initialize attribute buffers
    var program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);

    // Object vertex (v~~~: Vertex, c~~~`: Color)
    // Sky
    var vSky = [
        vec2(-1, 1),
        vec2(-1, -0.3),
        vec2(1, 1),
        vec2(1, -0.3),
    ];
    var cSky = [
        vec4(0.2, 0.39, 0.96, 0.5),
        vec4(0.2, 0.39, 0.96, 0.5),
        vec4(1.0, 0.98, 0.68, 1.0),
        vec4(0.0, 0.28, 0.68, 0.5),
    ];

    // Ground
    var vGround = [
        vec2(-1.0, -1.0),
        vec2(1.0, -1.0),
        vec2(-1.0, -0.2),
        vec2(1.0, -0.2),
    ];
    var cGround = [
        vec4(0.0, 0.29, 0.00, 0.8),
        vec4(0.0, 0.29, 0.00, 0.8),
        vec4(0.0, 0.99, 0.00, 0.5),
        vec4(0.0, 0.99, 0.00, 0.5),
    ];

    // Tree Leaf
    var vLeaf = [
        vec2(0, 0.25 - 0.3),
        vec2(-0.25, 0 - 0.3),
        vec2(0.25, 0 - 0.3),

        vec2(0, 0.25 - 0.15),
        vec2(-0.25, 0 - 0.15),
        vec2(0.25, 0 - 0.15),

        vec2(0, 0.25),
        vec2(-0.25, 0),
        vec2(0.25, 0),
    ];
    var cLeaf = [
        vec4(0.0, 0.8, 0.0, 1.0),
        vec4(0.0, 0.7, 0.0, 1.0),
        vec4(0.0, 0.7, 0.0, 1.0),

        vec4(0.0, 0.9, 0.0, 1.0),
        vec4(0.0, 0.8, 0.0, 1.0),
        vec4(0.0, 0.8, 0.0, 1.0),

        vec4(0.0, 1.0, 0.0, 1.0),
        vec4(0.0, 0.9, 0.0, 1.0),
        vec4(0.0, 0.9, 0.0, 1.0),
    ];

    // Pillar
    var vPillar = [
        vec2(-0.08, 0 - 0.5), // v0
        vec2(0.08, 0 - 0.5), // v1
        vec2(-0.08, 0.2 - 0.5), // v2
        vec2(0.08, 0.2 - 0.5), // v3
    ];
    var cPillar = [
        vec4(0.5, 0.25, 0.0, 1),
        vec4(0.5, 0.25, 0.0, 1),
        vec4(0.5, 0.25, 0.0, 1),
        vec4(0.5, 0.25, 0.0, 1),
    ];

    // Cloud Vertex
    var vCloud = [
        vec2(0.00, 0.00),
        vec2(0.05, 0.1),
        vec2(0.13, 0.00),
        vec2(0.08, -0.1),
        vec2(0.03, -0.1),
        vec2(0.00, -0.03),
        vec2(-0.03, -0.07),
        vec2(-0.03, -0.07),
        vec2(-0.05, -0.1),
        vec2(-0.1, -0.1),
        vec2(-0.15, -0.08),
        vec2(-0.18, 0.03),
        vec2(-0.05, 0.1),
        vec2(-0.03, 0.06),
        vec2(0.05, 0.1),
    ];
    var cCloud = [
        vec4(0.0, 1.0, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
        vec4(0.0, 0.6, 1.0, 0.5),
    ]

    // Lake
    var vLake = [
        vec2(-0.714, -0.95), //0
        vec2(-0.780, -0.90), //1
        vec2(-0.780, -0.80), //2
        vec2(-0.714, -0.73), //3
        vec2(-0.60, -0.70), //4
        vec2(-0.141, -0.68), //5
        vec2(0.071, -0.73), //6
        vec2(0.142, -0.79), //7
        vec2(0.142, -0.87), //8
        vec2(0.071, -0.95), //9
    ];
    var cLake = [
        vec4(0, 0, 0.2, 1), //0
        vec4(0, 0, 0.3, 0.9), //1
        vec4(0, 0, 0.4, 0.86), //2
        vec4(0, 0, 1.0, 0.76), //3
        vec4(0, 0, 1.0, 0.68), //4
        vec4(0, 0, 1.0, 0.58), //5
        vec4(0, 0, 0.9, 0.66), //6
        vec4(0, 0, 0.8, 0.74), //7
        vec4(0, 0, 0.6, 0.86), //8
        vec4(0, 0, 0.3, 1), //9
    ];

    // Stone
    var vStone = [
        vec2(0.10, -0.92), //0
        vec2(0.10, -0.80), //1
        vec2(0.17, -0.73), //2
        vec2(0.30, -0.73), //3
        vec2(0.40, -0.83), //4
        vec2(0.40, -0.87), //5
        vec2(0.37, -0.92), //6
    ];
    var cStone = [
        vec4(0.8, 0.8, 0.8, 1), //0
        vec4(0.8, 0.8, 0.8, 1), //1
        vec4(0.8, 0.8, 0.8, 0.97), //2
        vec4(0.8, 0.8, 0.8, 0.9), //3
        vec4(0.8, 0.8, 0.8, 0.95), //4
        vec4(0.8, 0.8, 0.8, 1), //5
        vec4(0.8, 0.8, 0.8, 1), //5
    ];

    // Loop
    var vLoop1 = [
        vec2(0.44, 0.00), //0
        vec2(0.00, 0.00), //1
        vec2(0.10, 0.065), //2
        vec2(0.18, 0.2), //3
        vec2(0.70, 0.2), //4
        vec2(0.78, 0.065), //5
        vec2(0.88, 0.00), //6
    ];
    var cLoop1 = [
        vec4(99 / 255, 103 / 255, 112 / 255, 1.0), //0
        vec4(99 / 255, 103 / 255, 112 / 255, 1.0), //1
        vec4(99 / 255, 103 / 255, 112 / 255, 0.9), //2
        vec4(99 / 255, 103 / 255, 112 / 255, 0.8), //3
        vec4(99 / 255, 103 / 255, 112 / 255, 0.8), //4
        vec4(99 / 255, 103 / 255, 112 / 255, 0.9), //5
        vec4(99 / 255, 103 / 255, 112 / 255, 1.0), //6
    ];
    var vLoop2=[
        vec2(0.18, 0.2), //0
        vec2(0.70, 0.2), //1
        vec2(0.70, 0.18), //2

        vec2(0.18, 0.2), //0
        vec2(0.70, 0.18), //2
        vec2(0.18, 0.18), //3

        vec2(0.18, 0.2), //0
        vec2(0.18, 0.18), //3
        vec2(0.10, 0.065), //4

        vec2(0.18, 0.18), //3
        vec2(0.10, 0.065), //4
        vec2(0.12, 0.065), //5

        vec2(0.10, 0.065), //4
        vec2(0.12, 0.065), //5
        vec2(0.00, 0.00), //6

        vec2(0.70, 0.2), //1
        vec2(0.70, 0.18), //2
        vec2(0.78, 0.065), //7

        vec2(0.70, 0.18), //2
        vec2(0.78, 0.065), //7
        vec2(0.76, 0.065), //8

        vec2(0.78, 0.065), //7
        vec2(0.76, 0.065), //8
        vec2(0.88, 0.00), //9

    ];
    var cLoop2=[
        vec4(1,1,1,1),
        vec4(1,1,1,1),
        vec4(1,1,1,1),

        vec4(1,1,1,1),
        vec4(1,1,1,1),
        vec4(1,1,1,1),

        vec4(1,1,1,1),
        vec4(1,1,1,1),
        vec4(1,1,1,1),

        vec4(1,1,1,1),
        vec4(1,1,1,1),
        vec4(1,1,1,1),

        vec4(1,1,1,1),
        vec4(1,1,1,1),
        vec4(1,1,1,1),

        vec4(1,1,1,1),
        vec4(1,1,1,1),
        vec4(1,1,1,1),

        vec4(1,1,1,1),
        vec4(1,1,1,1),
        vec4(1,1,1,1),

        vec4(1,1,1,1),
        vec4(1,1,1,1),
        vec4(1,1,1,1),
    ];

    var vLoop3=[
        vec2(0.18, 0.22), //0
        vec2(0.18, 0.18), //1
        vec2(0.20, 0.18), //2

        vec2(0.18, 0.22), //0
        vec2(0.20, 0.18), //2
        vec2(0.20, 0.22), //3

        vec2(0.70, 0.22), //4
        vec2(0.70, 0.18), //5
        vec2(0.68, 0.18), //6

        vec2(0.70, 0.22), //4
        vec2(0.68, 0.22), //7
        vec2(0.68, 0.18), //6

    ];
    var cLoop3=[
        vec4(214 / 255, 175 / 255, 105 / 255, 1),
        vec4(214 / 255, 175 / 255, 105 / 255, 1),
        vec4(214 / 255, 175 / 255, 105 / 255, 1),

        vec4(214 / 255, 175 / 255, 105 / 255, 1),
        vec4(214 / 255, 175 / 255, 105 / 255, 1),
        vec4(214 / 255, 175 / 255, 105 / 255, 1),

        vec4(214 / 255, 175 / 255, 105 / 255, 1),
        vec4(214 / 255, 175 / 255, 105 / 255, 1),
        vec4(214 / 255, 175 / 255, 105 / 255, 1),

        vec4(214 / 255, 175 / 255, 105 / 255, 1),
        vec4(214 / 255, 175 / 255, 105 / 255, 1),
        vec4(214 / 255, 175 / 255, 105 / 255, 1),
    ];

    // Bar1
    var vBar1 = [
        vec2(0.10, 0.000), //0
        vec2(0.17, 0.000), //1
        vec2(0.17, -0.100), //2
        vec2(0.10, -0.100), //3
    ];
    var cBar1_cFloor = [
        vec4(88 / 255, 53 / 255, 52 / 255, 1), //0
        vec4(88 / 255, 53 / 255, 52 / 255, 1), //1
        vec4(88 / 255, 53 / 255, 52 / 255, 1), //2
        vec4(88 / 255, 53 / 255, 52 / 255, 1), //3
    ];
    // Bar2
    var vBar2 = [
        vec2(0.10, -0.100), //0
        vec2(0.17, -0.100), //1
        vec2(0.17, -0.265), //2
        vec2(0.10, -0.265), //3
    ];
    var cBar2_cBase = [
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //0
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //1
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //2
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //3
    ];

    // Floor
    var vFloor=[
        vec2(0.07, -0.10), //0
        vec2(0.07, -0.15), //1
        vec2(0.81, -0.15), //2
        vec2(0.81, -0.10), //4
    ];

    // BaseStoneYellow
    var vBaseStoneY=[
        vec2(0.12, -0.111), //0
        vec2(0.12, -0.135), //1
        vec2(0.15, -0.135), //2
        vec2(0.15, -0.11), //4
    ];

    var cBaseStoneY=[
        vec4(250 / 255, 244 / 255, 192 / 255, 1),
        vec4(250 / 255, 244 / 255, 192 / 255, 1),
        vec4(250 / 255, 244 / 255, 192 / 255, 1),
        vec4(250 / 255, 244 / 255, 192 / 255, 1),
    ];

    // Offset information
    var count;
    var tran = [0, 0];

    // Ground
    varyingDraw(program, vGround, cGround, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_STRIP)

    // Sky
    varyingDraw(program, vSky, cSky, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_STRIP)

    // Tree 1, 2
    tran = [[-0.14, 0.18], [-0.73, 0.095], [-0.4, -0.1]];
    for (count = 0; count < tran.length; count++) {
        varyingDraw(program, vPillar, cPillar, [tran[count][0], tran[count][1], 0.0, 1], gl.TRIANGLE_STRIP)
        varyingDraw(program, vLeaf, cLeaf, [tran[count][0], tran[count][1], 0.0, 1], gl.TRIANGLES)
    }

    // Cloud 1, 2, 3
    tran = [[-0.8, 0.6], [-0.1, 0.7], [0.68, 0.5]];
    for (count = 0; count < tran.length; count++) {
        varyingDraw(program, vCloud, cCloud, [tran[count][0], tran[count][1], 0.0, 1], gl.TRIANGLE_FAN)
        varyingDraw(program, vCloud, cCloud, [tran[count][0] + 0.1, tran[count][1], 0.0, 1], gl.TRIANGLE_FAN)
    }

    // Lake
    tran = [0.1, 0]
    varyingDraw(program, vLake, cLake, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)

    // Stone
    tran = [0.02, -0.03]
    varyingDraw(program, vStone, cStone, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)// Stone

    // Loop
    tran = [0.1, -0.175]
    varyingDraw(program, vLoop1, cLoop1, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)
    varyingDraw(program, vLoop2, cLoop2, [tran[0], tran[1], 0.0, 1], gl.TRIANGLES)
    varyingDraw(program, vLoop3, cLoop3, [tran[0], tran[1], 0.0, 1], gl.TRIANGLES)

    // Bar
    tran = [[0.1, -0.175], [0.3, -0.175], [0.5, -0.175], [0.7, -0.175]]
    for (count = 0; count < tran.length; count++) {
        varyingDraw(program, vBar1, cBar1_cFloor, [tran[count][0], tran[count][1], 0.0, 1], gl.TRIANGLE_FAN)
        varyingDraw(program, vBar2, cBar2_cBase, [tran[count][0], tran[count][1], 0.0, 1], gl.TRIANGLE_FAN)
    }

    // Floor
    tran = [0.1, -0.175]
    varyingDraw(program, vFloor, cBar1_cFloor, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)

    // Base Stone
    tran = [0.1, -0.325]
    varyingDraw(program, vFloor, cBar2_cBase, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)

    // Base Stone Yellow
    tran = [0.1, -0.325]
    for (count = 0; count < 7; count++) {
    varyingDraw(program, vBaseStoneY, cBaseStoneY, [tran[0]+(count/10), tran[1], 0.0, 1], gl.TRIANGLE_FAN)
    }
}

function varyingDraw(program, v, color, offset, type) {
    // Vertex
    var vertexPositionBufferId = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexPositionBufferId);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(v), gl.STATIC_DRAW);

    var vPosition = gl.getAttribLocation(program, "vPosition");
    gl.vertexAttribPointer(vPosition, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);

    // Color
    var vertexColorBufferId = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexColorBufferId);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(color), gl.STATIC_DRAW);

    var vColor = gl.getAttribLocation(program, "vColor");
    gl.vertexAttribPointer(vColor, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vColor);

    // Offset
    var uOffset = gl.getUniformLocation(program, "uOffset")
    gl.uniform4fv(uOffset, [offset[0], offset[1], offset[2], 0])

    // Render part
    gl.drawArrays(type, 0, v.length);
}