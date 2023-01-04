import React, {useEffect, useState} from 'react'
import EditProfile from '../components/EditProfile'
import Topbar from '../components/Topbar'
import Sidebar from '../components/Sidebar'
import Posts from '../components/Posts'
import { useContext } from 'react';
import {Auth} from '../context/AuthContext'
import axios from 'axios';
import '../styles/home.css'
import Rightbar from '../components/Rightbar'
import SearchBox from '../components/SearchBox'
import CreatePost from '../components/CreatePost'

/*  

1 - db query 
    if(img) -> show home
    else show editPage  
        db quesry
        if(img) -> redirect to home
        else try again

*/



const Home = () => {

    const {curr_user, setCurr_user} = useContext(Auth)
    const [showCreatePost, setShowCreatePost] = useState(false)

    useEffect(() => {
        const data = window.localStorage.getItem('Socially_Current_User')
        if(data !== null) setCurr_user(JSON.parse(data))
    }, [])

    return (
        <>
        {
            (curr_user && curr_user.dateOfBirth !== "")
            ?
            <>
                <div className='topbar-container'>
                    <Topbar />
                </div>

                <div className='home-container'>
                    <div className='main-content'>
                        <div className='sidebar-container'>
                            <Sidebar />
                        </div>
                        <div className='posts-container'>
                            <SearchBox />
                            <div className='posts-heading'>
                                <div className='first'><div>Welcome {curr_user && curr_user.username}!</div></div>
                                <div className='second'><button onClick={() => setShowCreatePost(showCreatePost => !showCreatePost)}>Create a Post</button></div>
                            </div>
                            <div>
                                <Posts isProfile={false}/>
                            </div>
                        </div>
                        <div className='rightbar-container'>
                            <Rightbar />
                        </div>
                    </div>
                    {
                        (showCreatePost 
                        && 
                        <CreatePost showCreatePost={showCreatePost} setShowCreatePost={setShowCreatePost} />
                        )
                    }
                </div>
            </>
            :
            <EditProfile />
        }
        </>
    )
}

export default Home