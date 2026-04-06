/* Live Clock */
function updateClock(){
    const now = new Date();
    document.getElementById("clock").innerText =
        now.toLocaleString('en-IN',{weekday:'long', hour:'2-digit', minute:'2-digit'});
}
setInterval(updateClock,1000);
updateClock();

/* Animated Counter */
function animate(id,start,end,speed){
    let val=start;
    const el=document.getElementById(id);
    const timer=setInterval(()=>{
        val++;
        el.innerText=val;
        if(val>=end) clearInterval(timer);
    },speed);
}



/* Progress Bar */
setTimeout(()=>{
    document.getElementById("progress").style.width="82%";
    document.getElementById("progress").innerText="82%";
},600);
