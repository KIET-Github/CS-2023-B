import React, {useContext, useEffect, useState} from 'react'
import { Auth } from '../context/AuthContext'
import '../styles/createPost.css'
import axios from 'axios'

const CreatePost = ({showCreatePost, setShowCreatePost}) => {

    const {curr_user, setCurr_user} = useContext(Auth)
    const [file, setFile] = useState();

    useEffect(() => {
        const data = window.localStorage.getItem('Socially_Current_User')
        if(data !== null) setCurr_user(JSON.parse(data))
        // console.log('1')
    }, [])

    const [userCreatePost, setUserCreatePost] = useState({
        caption: "",
        postPic: ""
    })
  
    const handleChange = (e) => {
        setUserCreatePost({
           ...userCreatePost,
           [e.target.name]: e.target.value
      })
    }

    const handleClick = async (e) => {
        e.preventDefault();

  
        const data = new FormData();
        const fileName = Date.now() + file.name;
        data.append("name", fileName);
        data.append("file", file);

        try {
          await axios.post("http://localhost:9002/api/fileUpload", data)
        } catch (err) {

        }

        const obj = {
          id: curr_user._id,
          profilePic: curr_user.profilePic,
          caption: userCreatePost.caption,
          postPic: fileName
        }

        await axios.post("http://localhost:9002/api/createPost", obj)
        .then(res => alert(res.data.message))
        .catch(err => console.log(err))

        setShowCreatePost(false)
        window.location.reload()
  }

    return (
        <div className='create-post'>
            <form>
              <div className="container">
                <h1>Create a Post</h1>
                <div className="content">
                    <div className="input-field">
                        <input type="text"
                              placeholder={`What's on your mind`} 
                              name="caption" 
                              value={userCreatePost.caption}
                              onChange={handleChange}
                              required
                        />
                    </div>
                    <div className="input-field">
                        <input type="file"
                              id="file"
                              accept=".png,.jpeg,.jpg"
                              required
                              onChange={(e) => setFile(e.target.files[0])}
                        />
                    </div>
                    <div className="input-field">
                        <input type="submit"
                              value="Create"
                              onClick={handleClick}
                              style={{cursor: 'pointer'}}
                        />
                    </div>
                </div>
              </div>
            </form>
        </div>
    )
}

export default CreatePost