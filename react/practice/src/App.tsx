import { useState } from 'react';
import './App.css'
import UserCard from './component/Greetings'
import Welcome from './component/welcome'
import CounterDisplay from './component/CounterDisplay';
import CounterButtons from './component/CounterButtons';

function App() {
   const [count, setCount] = useState<number>(0);

  return (
    <>
      <div>

        <p>This is my first step</p>
        <h1>Welcome to React program</h1>
        <UserCard names="SAI"></UserCard>
        <Welcome name="SAI" message="Good Morning" age={2020}></Welcome>
        </div>

        <h1>Lifting State Up Example</h1>

      <CounterDisplay count={count} />

      <CounterButtons
        count={count}
        setCount={setCount}/>
    </>
  )
}

export default App
