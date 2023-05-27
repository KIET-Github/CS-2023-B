import React, { useEffect, useState, useContext } from 'react'
import '../styles/topbar.css'
import {Auth} from '../context/AuthContext'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'
import { SearchQuery } from '../context/SearchQueryContext'
import ChatIcon from '@mui/icons-material/Chat';

const Topbar = () => {

    const navigate = useNavigate()

    const {curr_user, setCurr_user} = useContext(Auth)
    const {query, setQuery} = useContext(SearchQuery)

    // const [data, setData] = useState([])

    const handleLogout = () => {
        window.localStorage.removeItem('Socially_Current_User')
        setCurr_user({})
        navigate('/')
    }

    useEffect(() => {
        const data = window.localStorage.getItem('Socially_Current_User')
        if(data !== null) setCurr_user(JSON.parse(data))
    }, [])

    return (

        <div className='navbar-container'>
            <div className='navbar'>
        
                <div className='logo' onClick={() => navigate('/')}>Socially</div>
                <div className='search'>
                    <input type="text" placeholder='Search Friends...' onChange={(e) => setQuery(e.target.value)}></input>
                </div>

                <div className='profile-photo'>
                    <img 
                        src={curr_user && "http://localhost:9002/images/" + curr_user.profilePic} 
                        onClick={() => navigate('/profile')}>
                    </img>
                </div>
                <div className='chat-icon' onClick={() => navigate('/chatPage')}>
                    <ChatIcon></ChatIcon>
                </div>
                <div className='logout'>
                    <button onClick={handleLogout}>Logout</button>
                </div>
            </div>
        </div>
                
    )
}

export default Topbar