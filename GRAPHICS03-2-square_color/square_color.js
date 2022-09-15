var gl;
var points;

window.onload = function init() {
    var canvas = document.getElementById("gl-canvas");
    gl = WebGLUtils.setupWebGL(canvas);
    if (!gl) {
        alert("WebGL isn't available");
    }
    // Vertex position
    var vertices = [
        // leaf
        vec2(-0.5, 0.5),  // v1
        vec2(-0.5, -0.5), // v2
        vec2(0.5, -0.5),  // v3

        vec2(-0.5, 0.5),  // v1
        vec2(0.5, -0.5),  // v3
        vec2(0.5, 0.5),   // v4
    ]

    // Vertex color (R, G, B, A)
    var colors = [
        // leaf
        vec4(0.5, 1, 0.5, 1),
        vec4(0, 0, 0.5, 1),
        vec4(1, 0, 0.5, 1),

        vec4(0.5, 1, 0.5, 1),
        vec4(1, 0, 0.5, 1),
        vec4(1, 1, 0.5, 1),

    ]

    //  Configure WebGL
    gl.viewport(0, 0, canvas.width, canvas.height);
    gl.clearColor(0.0, 0.0, 0.0, 1.0);

    // Load shaders and initialize attribute buffers
    var program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);

    /*----------------------------------*/
    /*-vertex Position------------------*/
    /*----------------------------------*/

    /// triangle vertex buffer
    var vertexPositionBufferId = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexPositionBufferId);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(vertices), gl.STATIC_DRAW);

    var vPosition = gl.getAttribLocation(program, "vPosition");``
    gl.vertexAttribPointer(vPosition, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);

    /*----------------------------------*/
    /*-vertex Color---------------------*/
    /*----------------------------------*/

    var vertexColorBufferId = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexColorBufferId);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(colors), gl.STATIC_DRAW);

    var vColor = gl.getAttribLocation(program, "vColor");
    gl.vertexAttribPointer(vColor, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vColor)
    render();
};

function render() {
    gl.clear(gl.COLOR_BUFFER_BIT);
    gl.drawArrays(gl.TRIANGLE_STRIP, 0, 6);
}