window.onload = function init() {
    const canvas = document.getElementById("gl-canvas");
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    const renderer = new THREE.WebGLRenderer({canvas});
    renderer.setSize(canvas.width, canvas.height);

    const scene = new THREE.Scene();
    scene.background = new THREE.Color(0x000000);

    camera = new THREE.PerspectiveCamera(75, canvas.width / canvas.height, 0.1, 1000);
    camera.position.x = 20;
    camera.position.y = 40;
    camera.position.z = 40;

    const controls = new THREE.OrbitControls(camera, renderer.domElement);

    light0 = new THREE.AmbientLight(0x404040, 0.5);
    scene.add(light0);

    light1 = new THREE.PointLight(0xc4c4c4, 1);
    light1.position.set(1000, 1000, 300);
    scene.add(light1);

    light2 = new THREE.PointLight(0xc4c4c4, 3);
    light2.position.set(-1000, -1000, 300);
    scene.add(light2);


    const loader = new THREE.GLTFLoader();
    loader.load('./model/Bunny Lamp.gltf', function (gltf) {
        bunny = gltf.scene.children[0];
        bunny.scale.set(2, 2, 2);
        scene.add(gltf.scene);
        animate();
    }, undefined, function (error) {
        console.error(error);
    });

    function animate() {
        controls.update();
        bunny.rotation.x += 0.003;
        renderer.render(scene, camera);
        requestAnimationFrame(animate);
    }

}


