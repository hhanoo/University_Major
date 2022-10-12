let gl;
let points;

// move cloud flag
let cloud_flag = false;
let cloud_direction = false;
let cloud_up_down = false;
let cloud_move = 0.0;
let cloud_vibration = 0.0;
let click_location = []

window.onload = function init() {
    let canvas = document.getElementById("gl-canvas");
    gl = WebGLUtils.setupWebGL(canvas);
    if (!gl) {
        alert("WebGL isn't available");
    }

    //  Configure WebGL
    gl.viewport(0, 0, canvas.width, canvas.height);
    gl.clearColor(0.0, 0.0, 0.0, 0.0);

    // Load shaders and initialize attribute buffers
    let program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);

    // Ground
    ground(program, [0, 0])

    // Sky
    sky(program, [0, 0])

    // Tree 1, 2
    tree(program, [[-0.14, 0.18], [-0.73, 0.095], [-0.4, -0.1]])

    // Cloud 1, 2, 3
    cloud(program, [[-0.8, 0.68], [-0.1, 0.78], [0.68, 0.58]], cloud_move, cloud_vibration)

    // Lake
    lake(program, [0.1, 0])

    // Stone
    stone(program, [0.02, -0.03])

    // ceiling
    ceiling(program, [0.1, -0.175])

    // Bar
    bar(program, [[0.1, -0.175], [0.3, -0.175], [0.5, -0.175], [0.7, -0.175]])

    // Floor
    floor(program, [0.1, -0.175])

    // Base Stone
    base_stone(program, [0.1, -0.325])

    // Base Stone Yellow
    base_stone_yellow(program, [0.1, -0.325])

    // Setting button cloud moving true or not
    document.getElementById("cloud-flag").onclick = function () {
        cloud_flag = !cloud_flag;
        console.log('cloud-flag: ', cloud_flag)
    }

    // Setting button cloud move direction
    document.getElementById("cloud-direct").onclick = function () {
        cloud_direction = !cloud_direction;
        console.log('cloud_direction: ', cloud_direction)
    }

    // Setting mouse click listener
    canvas.addEventListener("mousedown", event => {
        let canvasX = (2 * event.clientX) / canvas.width - 1 - 0.035
        let canvasY = 2 * (canvas.height - event.clientY) / canvas.height - 1 + 0.2
        click_location.push({x: canvasX, y: canvasY})
    })

    //Draw click sunshine
    for (let i = 0; i < click_location.length; i++) {
        sunshine(program, [click_location[i].x, click_location[i].y])
    }


    // Cloud moving offset setting
    if (cloud_flag) {
        // Moving cloud left, right
        if (cloud_direction)
            cloud_move += 0.001;
        else
            cloud_move -= 0.001;

        // Moving cloud up, down
        if (cloud_up_down) {
            cloud_vibration += 0.0005;
            if (cloud_vibration > 0.03)
                cloud_up_down = !cloud_up_down;
        } else {
            cloud_vibration -= 0.0005;
            if (cloud_vibration < -0.03)
                cloud_up_down = !cloud_up_down;
        }
    }


    setTimeout(requestAnimFrame(init), 10)
}

function varyingDraw(program, v, color, offset, type) {
    // Vertex
    let vertexPositionBufferId = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexPositionBufferId);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(v), gl.STATIC_DRAW);

    let vPosition = gl.getAttribLocation(program, "vPosition");
    gl.vertexAttribPointer(vPosition, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);

    // Color
    let vertexColorBufferId = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexColorBufferId);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(color), gl.STATIC_DRAW);

    let vColor = gl.getAttribLocation(program, "vColor");
    gl.vertexAttribPointer(vColor, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vColor);

    // Offset
    let uOffset = gl.getUniformLocation(program, "uOffset")
    gl.uniform4fv(uOffset, [offset[0], offset[1], 0, 0])

    // Render part
    gl.drawArrays(type, 0, v.length);
}

// Ground
function ground(program, tran) {
    let vertex = [
        vec2(-1.0, -1.0),
        vec2(1.0, -1.0),
        vec2(-1.0, -0.2),
        vec2(1.0, -0.2),
    ];
    let color = [
        vec4(0.0, 0.29, 0.00, 0.8),
        vec4(0.0, 0.29, 0.00, 0.8),
        vec4(0.0, 0.99, 0.00, 0.5),
        vec4(0.0, 0.99, 0.00, 0.5),
    ];

    varyingDraw(program, vertex, color, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_STRIP)
}

// Sky
function sky(program, tran) {
    let vertex = [
        vec2(-1, 1),
        vec2(-1, -0.3),
        vec2(1, 1),
        vec2(1, -0.3),
    ];
    let color = [
        vec4(0.2, 0.39, 0.96, 0.5),
        vec4(0.2, 0.39, 0.96, 0.5),
        vec4(1.0, 0.98, 0.68, 1.0),
        vec4(0.0, 0.28, 0.68, 0.5),
    ];

    varyingDraw(program, vertex, color, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_STRIP)
}

// Three
function tree(program, tran) {
    let vLeaf = [
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
    let cLeaf = [
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

    let vPillar = [
        vec2(-0.08, 0 - 0.5), // v0
        vec2(0.08, 0 - 0.5), // v1
        vec2(-0.08, 0.2 - 0.5), // v2
        vec2(0.08, 0.2 - 0.5), // v3
    ];
    let cPillar = [
        vec4(0.5, 0.25, 0.0, 1),
        vec4(0.5, 0.25, 0.0, 1),
        vec4(0.5, 0.25, 0.0, 1),
        vec4(0.5, 0.25, 0.0, 1),
    ];

    for (let count = 0; count < tran.length; count++) {
        varyingDraw(program, vPillar, cPillar, [tran[count][0], tran[count][1], 0.0, 1], gl.TRIANGLE_STRIP)
        varyingDraw(program, vLeaf, cLeaf, [tran[count][0], tran[count][1], 0.0, 1], gl.TRIANGLES)
    }
}

// Cloud
function cloud(program, tran, cloud_move, cloud_vibration) {
    let vertex = [
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
    let color = [
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

    for (let count = 0; count < tran.length; count++) {
        varyingDraw(program, vertex, color, [tran[count][0] + cloud_move, tran[count][1] + cloud_vibration, 0.0, 1], gl.TRIANGLE_FAN)
        varyingDraw(program, vertex, color, [tran[count][0] + 0.1 + cloud_move, tran[count][1] + cloud_vibration, 0.0, 1], gl.TRIANGLE_FAN)
    }
}

// Lake
function lake(program, tran) {
    let vertex = [
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
    let color = [
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

    varyingDraw(program, vertex, color, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)
}

// Stone
function stone(program, tran) {
    let vertex = [
        vec2(0.10, -0.92), //0
        vec2(0.10, -0.80), //1
        vec2(0.17, -0.73), //2
        vec2(0.30, -0.73), //3
        vec2(0.40, -0.83), //4
        vec2(0.40, -0.87), //5
        vec2(0.37, -0.92), //6
    ];
    let color = [
        vec4(0.8, 0.8, 0.8, 1), //0
        vec4(0.8, 0.8, 0.8, 1), //1
        vec4(0.8, 0.8, 0.8, 0.97), //2
        vec4(0.8, 0.8, 0.8, 0.9), //3
        vec4(0.8, 0.8, 0.8, 0.95), //4
        vec4(0.8, 0.8, 0.8, 1), //5
        vec4(0.8, 0.8, 0.8, 1), //5
    ];
    varyingDraw(program, vertex, color, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)// Stone
}

// Ceiling
function ceiling(program, tran) {
    let vLoop1 = [
        vec2(0.44, 0.00), //0
        vec2(0.00, 0.00), //1
        vec2(0.10, 0.065), //2
        vec2(0.18, 0.2), //3
        vec2(0.70, 0.2), //4
        vec2(0.78, 0.065), //5
        vec2(0.88, 0.00), //6
    ];
    let cLoop1 = [
        vec4(99 / 255, 103 / 255, 112 / 255, 1.0), //0
        vec4(99 / 255, 103 / 255, 112 / 255, 1.0), //1
        vec4(99 / 255, 103 / 255, 112 / 255, 0.9), //2
        vec4(99 / 255, 103 / 255, 112 / 255, 0.8), //3
        vec4(99 / 255, 103 / 255, 112 / 255, 0.8), //4
        vec4(99 / 255, 103 / 255, 112 / 255, 0.9), //5
        vec4(99 / 255, 103 / 255, 112 / 255, 1.0), //6
    ];
    let vLoop2 = [
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
    let cLoop2 = [
        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),

        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),

        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),

        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),

        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),

        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),

        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),

        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),
        vec4(1, 1, 1, 1),
    ];

    let vLoop3 = [
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
    let cLoop3 = [
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

    varyingDraw(program, vLoop1, cLoop1, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)
    varyingDraw(program, vLoop2, cLoop2, [tran[0], tran[1], 0.0, 1], gl.TRIANGLES)
    varyingDraw(program, vLoop3, cLoop3, [tran[0], tran[1], 0.0, 1], gl.TRIANGLES)

}

// Bar
function bar(program, tran) {
    let vBar1 = [
        vec2(0.10, 0.000), //0
        vec2(0.17, 0.000), //1
        vec2(0.17, -0.100), //2
        vec2(0.10, -0.100), //3
    ];
    let cBar1 = [
        vec4(88 / 255, 53 / 255, 52 / 255, 1), //0
        vec4(88 / 255, 53 / 255, 52 / 255, 1), //1
        vec4(88 / 255, 53 / 255, 52 / 255, 1), //2
        vec4(88 / 255, 53 / 255, 52 / 255, 1), //3
    ];
    // Bar2
    let vBar2 = [
        vec2(0.10, -0.100), //0
        vec2(0.17, -0.100), //1
        vec2(0.17, -0.265), //2
        vec2(0.10, -0.265), //3
    ];
    let cBar2 = [
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //0
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //1
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //2
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //3
    ];

    for (let count = 0; count < tran.length; count++) {
        varyingDraw(program, vBar1, cBar1, [tran[count][0], tran[count][1], 0.0, 1], gl.TRIANGLE_FAN)
        varyingDraw(program, vBar2, cBar2, [tran[count][0], tran[count][1], 0.0, 1], gl.TRIANGLE_FAN)
    }
}

// Floor
function floor(program, tran) {
    let vertex = [
        vec2(0.07, -0.10), //0
        vec2(0.07, -0.15), //1
        vec2(0.81, -0.15), //2
        vec2(0.81, -0.10), //4
    ];

    let color = [
            vec4(88 / 255, 53 / 255, 52 / 255, 1), //0
            vec4(88 / 255, 53 / 255, 52 / 255, 1), //1
            vec4(88 / 255, 53 / 255, 52 / 255, 1), //2
            vec4(88 / 255, 53 / 255, 52 / 255, 1), //3
        ]
    ;
    varyingDraw(program, vertex, color, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)
}

// Base Stone
function base_stone(program, tran) {
    let vertex = [
        vec2(0.07, -0.10), //0
        vec2(0.07, -0.15), //1
        vec2(0.81, -0.15), //2
        vec2(0.81, -0.10), //4
    ];

    let color = [
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //0
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //1
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //2
        vec4(177 / 255, 176 / 255, 163 / 255, 1), //3
    ]
    varyingDraw(program, vertex, color, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)
}

// Base Stone Yellow
function base_stone_yellow(program, tran) {
    let vertex = [
        vec2(0.12, -0.111), //0
        vec2(0.12, -0.135), //1
        vec2(0.15, -0.135), //2
        vec2(0.15, -0.110), //4
    ];

    let color = [
        vec4(250 / 255, 244 / 255, 192 / 255, 1),
        vec4(250 / 255, 244 / 255, 192 / 255, 1),
        vec4(250 / 255, 244 / 255, 192 / 255, 1),
        vec4(250 / 255, 244 / 255, 192 / 255, 1),
    ];
    for (let count = 0; count < 7; count++) {
        varyingDraw(program, vertex, color, [tran[0] + (count / 10), tran[1], 0.0, 1], gl.TRIANGLE_FAN)
    }
}

// Sunshine
function sunshine(program, tran) {
    let center = [
        vec2(0.0, 0.0), //0
        vec2(-0.005, 0.01), //1
        vec2(-0.01, 0.005),//2
        vec2(-0.01, -0.005), //3
        vec2(-0.005, -0.01), //4
        vec2(0.005, -0.01), //5
        vec2(0.01, -0.005), //6
        vec2(0.01, 0.005), //7
        vec2(0.005, 0.01), //8
        vec2(-0.005, 0.01), //1
    ];
    let center_color = [
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //0
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //1
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //2
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //3
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //4
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //5
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //6
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //7
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //8
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //1
    ];
    let edge = [
        vec2(0.005, 0.01), //8
        vec2(0.0, 0.072), //8-1
        vec2(-0.005, 0.01), //1

        vec2(-0.005, 0.01), //1
        vec2(-0.05, 0.05), //1-2
        vec2(-0.01, 0.005), //2

        vec2(-0.01, 0.005), //2
        vec2(-0.072, 0.0), //2-3
        vec2(-0.01, -0.005), //3

        vec2(-0.01, -0.005), //3
        vec2(-0.05, -0.05),//3-4
        vec2(-0.005, -0.01), //4

        vec2(-0.005, -0.01), //4
        vec2(0.0, -0.072),//4-5
        vec2(0.005, -0.01), //5

        vec2(0.005, -0.01), //5
        vec2(0.05, -0.05), //5-6
        vec2(0.01, -0.005), //6

        vec2(0.01, -0.005), //6
        vec2(0.072, 0.0), //6-7
        vec2(0.01, 0.005), //7

        vec2(0.01, 0.005), //7
        vec2(0.05, 0.05),//7-8
        vec2(0.005, 0.01), //8
    ];
    let edge_color = [
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //8
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //8-1
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //1

        vec4(252 / 255, 249 / 255, 190 / 255, 1), //1
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //1-2
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //2

        vec4(252 / 255, 249 / 255, 190 / 255, 1), //2
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //2-3
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //3

        vec4(252 / 255, 249 / 255, 190 / 255, 1), //3
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //3-4
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //4

        vec4(252 / 255, 249 / 255, 190 / 255, 1), //4
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //4-5
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //5

        vec4(252 / 255, 249 / 255, 190 / 255, 1), //5
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //5-6
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //6

        vec4(252 / 255, 249 / 255, 190 / 255, 1), //6
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //6-7
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //7

        vec4(252 / 255, 249 / 255, 190 / 255, 1), //7
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //7-8
        vec4(252 / 255, 249 / 255, 190 / 255, 1), //8
    ];

    varyingDraw(program, center, center_color, [tran[0], tran[1], 0.0, 1], gl.TRIANGLE_FAN)
    varyingDraw(program, edge, edge_color, [tran[0], tran[1], 0.0, 1], gl.TRIANGLES)
}