
/* Popup */
function showWarning(){
    document.getElementById("popup").style.display="flex";
}
function closePopup(){
    document.getElementById("popup").style.display="none";
}

/* Animated Counters */
function counter(id, start, end, speed, suffix=""){
    let val = start;
    const el = document.getElementById(id);
    const timer = setInterval(() => {
        val++;
        el.innerText = val + suffix;
        if(val >= end) clearInterval(timer);
    }, speed);
}

counter("s1",0,500,5,"+");
counter("s2",0,1200,2,"+");
counter("s3",0,98,30,"%");
counter("s4",0,24,120,"/7");
