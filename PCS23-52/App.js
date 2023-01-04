import '../src/styles/App.css';
import EditProfile from './components/EditProfile';
import Topbar from './components/Topbar';
import Home from './pages/Home';
import Register from './pages/Register';
import Login from './pages/Login';
import {
  BrowserRouter as Router,
  Routes,
  Route
} from "react-router-dom";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { useContext, useEffect } from 'react';
import {Auth} from '../src/context/AuthContext'
import Profile from './pages/Profile';
import ChatPage from './pages/ChatPage';

function App() {

  const {curr_user, setCurr_user} = useContext(Auth)

  useEffect(() => {
      const data = window.localStorage.getItem('Socially_Current_User')
      if(data !== null) setCurr_user(JSON.parse(data))
  }, [])

  return (

    <Router>
      <Routes>
        <Route exact path="/" 
               element=
               {
                  (curr_user && curr_user._id) 
                  ?
                  <Home />  
                  : 
                  <Login />
               }>
        </Route>
        <Route exact path="/login" element={<Login />}></Route>
        <Route exact path="/register" element={<Register />}></Route>
        <Route exact path="/profile" element={<Profile />}></Route>
        <Route exact path="/chatPage" element={<ChatPage />}></Route>
      </Routes>

      <ToastContainer />
    </Router>
    // <Aise />

  );
}

export default App;
