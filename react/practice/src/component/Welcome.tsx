import { useState } from "react";



type welcomeProps={
    name:string;
    message:string;
    age:number;
}

function Welcome({name,message,age}:welcomeProps){
    const[count,setCount] = useState<number>(0);
    return (
         <>
         <p>Count : {count}</p>
         <button onClick={()=>setCount(count+1)}>Increment</button>
         <button onClick={()=>setCount(count-1)}>Decrement</button>
         <h1>{message}      {name} time {age}</h1>
         </>
    );
}
export default Welcome;