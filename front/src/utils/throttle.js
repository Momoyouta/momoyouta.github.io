export function throttle(fn,delay) {
    let timer=null;
    return (...args)=>{
        if(!timer){
            timer=setTimeout(()=>{
                fn.apply(this,args);
                timer = null;
            },delay);
        }
    }
}