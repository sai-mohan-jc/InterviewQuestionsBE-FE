import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import Header from './components/Header';
import Sidebar from './components/Sidebar';
import Accounts from './pages/Accounts';
import Dashboard from './pages/Dashboard';
import Transactions from './pages/Transactions';
import Profile from './pages/Profile';
import Transfer from './pages/Transfer';
import AIAssistant from './pages/AIAssistant';

function App() {
  return (
    <BrowserRouter>
    <div className="app">
<Header></Header>
<div className='main-container'>
  <Sidebar/>


  <Routes>
    <Route path="/" element={<Dashboard/>} />
    <Route path="/profile" element={<Profile/>} />
    <Route path="/accounts" element={<Accounts/>} />
    <Route path="/transfers" element={<Transfer/>} />
    <Route path="/transactions" element={<Transactions/>} />
    <Route path="/ai-assistant" element={<AIAssistant />} />
  </Routes>
</div>
    </div>
    </BrowserRouter>
    
  );
}

export default App;