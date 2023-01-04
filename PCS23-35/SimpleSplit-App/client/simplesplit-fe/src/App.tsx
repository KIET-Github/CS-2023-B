import React, { useState } from 'react';
import Login from './components/Login';
import Home from './components/Home';

const App: React.FC = () => {
  
  const [navigation, setNavigation] = useState("HOME");
  return (
    <div>
    {
      navigation === "HOME"
       ? <Home setNavigation={setNavigation}/>
       : navigation === "LOGIN"
         ? <Login setNavigation={setNavigation}/>
         : <Home setNavigation={setNavigation}/>
    }
      
    </div>
  );
}
export default App;
