import React, { useContext, useEffect, useState } from 'react'
import Posts from '../components/Posts'
import ProfileCover from '../components/ProfileCover'
import Rightbar from '../components/Rightbar'
import SearchBox from '../components/SearchBox'
import Sidebar from '../components/Sidebar'
import Topbar from '../components/Topbar'
import { Auth } from '../context/AuthContext'
import '../styles/profile.css'


const Profile = () => {
  
    const [currUser, setCurrUser] = useState()

    useEffect(() => {
        const getUser = () => {
            const data = window.localStorage.getItem('Socially_Current_User')
            if(data !== null) setCurrUser(JSON.parse(data))
        }
        getUser()
    }, [])
  
  
    return (




    <>
        <div className='topbar-container'>
            <Topbar />
        </div>

        <div className='profile-searchBox'>
            <SearchBox />
        </div>

        <div className='profile-container'>
            <div className='cover-photo'>
                <div className='cover-pic'>
                    <img src={currUser && "http://localhost:9002/images/" + currUser.coverPic}></img>
                </div>
                <div className='profile-pic'>
                    <img src={currUser && "http://localhost:9002/images/" + currUser.profilePic}></img>
                </div>
            </div>
            <div className='down-container'>
                <div className='leftbar'>leftbar</div>
                <div className='posts'><Posts isProfile={true}/></div>
                <div className='rightbar'><Rightbar /></div>
            </div>

        </div>

    </>
  )
}

export default Profile