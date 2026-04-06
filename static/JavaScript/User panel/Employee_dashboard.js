
window.onload = function(){

    const btn = document.getElementById("logoutBtn");
    if(!btn) return;

    const door = document.getElementById("door");
    const man = document.getElementById("man");

    btn.addEventListener("click", function(){

        // Door open
        door.style.transform = "rotateY(40deg)";

        // Man walk
        setTimeout(()=>{
            man.style.transform = "translateX(18px)";
        },200);

        // Man enter
        setTimeout(()=>{
            man.style.opacity = "0";
        },600);

        // Door close
        setTimeout(()=>{
            door.style.transform = "rotateY(0deg)";
        },900);

        // Redirect logout
        setTimeout(()=>{
            window.location.href="/logout";
        },1300);

    });

};