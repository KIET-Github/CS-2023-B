import React, { useEffect, useState } from 'react'
import axios from 'axios'
import '../styles/login.css'
import { useNavigate } from 'react-router-dom'
import { useContext } from 'react';
import {Auth} from '../context/AuthContext'
import { toast } from 'react-toastify';
const Login = () => {

    const {curr_user, setCurr_user} = useContext(Auth)

    const navigate = useNavigate()
    const [user, setUser] = useState({
        email: "",
        password: ""
    })

    const handleChange = (e) => {
       setUser({
            ...user,
            [e.target.name]: e.target.value
       })
    }

    const handleLogin = () => {
        axios.post("http://localhost:9002/api/login", user)
        .then(res => {
            toast.success(res.data.message)
            window.localStorage.setItem('Socially_Current_User', JSON.stringify(res.data.user))
            console.log("object is", res.data.user)
            setCurr_user(res.data.user)
            navigate("/")
        })
    }

    return (
        <>
            <div className="login-form">
                <h1>Login</h1>
                <div className="content">
                    <div className="input-field">
                        <input type="email" 
                            placeholder="Email" 
                            name="email" 
                            value={user.email}
                            onChange={handleChange} 
                        />
                    </div>
                    <div className="input-field">
                        <input type="password" 
                            placeholder="Password" 
                            name="password" 
                            value={user.password}
                            onChange={handleChange}
                        />
                    </div>
                {/* <a href="#" className="link">Forgot Your Password?</a> */}
                </div>
                <div className="action">
                    <button onClick={() => navigate('/register')}>Register</button>
                    <button onClick={handleLogin}>Log in</button>
                </div>
            </div>
        </>
    )
}

export default Login