var gl;

window.onload = function init() {
    var canvas = document.getElementById("gl-canvas");
    gl = WebGLUtils.setupWebGL(canvas);
    if (!gl) {
        alert("WebGL isn't available");
    }

    // Vertex position
    var vertices = [
        // leaf
        vec2(0, 1), // v0
        vec2(-1, 0), // v1
        vec2(1, 0), // v2

        // tree pillar
        vec2(0, 0), // v3
        vec2(0.6, 0), // v4
        vec2(0.6, 1), // v5
        vec2(0, 1), // v6
    ]

    //  Configure WebGL
    gl.viewport(0, 0, canvas.width, canvas.height);
    gl.clearColor(1.0, 1.0, 1.0, 1.0);

    // Load shaders and initialize attribute buffers
    var program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);

    // Load the data into the GPU
    var bufferId = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, bufferId);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(vertices), gl.STATIC_DRAW);

    // Associate out shader variables with our data buffer
    var vPosition = gl.getAttribLocation(program, "vPosition");
    var uOffset = gl.getUniformLocation(program, "uOffset");
    var uColor = gl.getUniformLocation(program, "uColor");


    gl.vertexAttribPointer(vPosition, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);

    render(uOffset, uColor);
};

function render(uOffset, uColor) {
    gl.clear(gl.COLOR_BUFFER_BIT);

    // leaf
    gl.uniform4f(uColor, 0.0, 1.0, 0.0, 1.0)
    gl.uniform4fv(uOffset, [0, 0, 0, 2])
    gl.drawArrays(gl.TRIANGLES, 0, 3);
    gl.uniform4fv(uOffset, [0, 1, 0, 2])
    gl.drawArrays(gl.TRIANGLES, 0, 3);
    gl.uniform4fv(uOffset, [0, 2, 0, 2])
    gl.drawArrays(gl.TRIANGLES, 0, 3);

    // pillar
    gl.uniform4f(uColor, 0.5, 0.25, 0.0, 1);
    gl.uniform4fv(uOffset, [-0.3, -1, 0, 2])
    gl.drawArrays(gl.TRIANGLE_FAN, 3, 4);
}

