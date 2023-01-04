import React, { useState } from 'react'
import axios from 'axios'
import '../styles/register.css'
import {useNavigate} from 'react-router-dom'

const Register = () => {

    const navigate = useNavigate()
    const [user, setUser] = useState({
        username: "",
        email: "",
        password: "",
        reEnterPassword: ""
    })

    const handleChange = (e) => {
       setUser({
            ...user,
            [e.target.name]: e.target.value
       })
    }

    const handleRegister = () => {
        const {username, email, password, reEnterPassword} = user

        if(username && email && password && (password === reEnterPassword)){
            axios.post("http://localhost:9002/api/register", user)
            .then(res => alert(res.message))
            navigate("/login")
        }else{
            alert("Invalid input")
        }
    }
    

    return (
        <>
            <div className="register-form">
                <h1>Register</h1>
                <div className="content">
                    <div className="input-field">
                        <input type="text" 
                            placeholder="Username" 
                            name="username" 
                            value={user.username}
                            onChange={handleChange} 
                        />
                    </div>
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
                    <div className="input-field">
                        <input type="password" 
                            placeholder='Re-Enter-Password'
                            name="reEnterPassword"
                            value={user.reEnterPassword}
                            onChange={handleChange}
                        />
                    </div>
                {/* <a href="#" className="link">Forgot Your Password?</a> */}
                </div>
                <div className="action">
                    <button onClick={() => navigate('/login')}>Log In</button>
                    <button onClick={handleRegister}>Register</button>
                </div>
            </div>


        </>
    )
}

export default Register