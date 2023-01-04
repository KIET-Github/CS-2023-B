import React, { useContext,useState, useEffect } from 'react'
import '../styles/editProfile.css'
import axios from 'axios'
import { Auth } from '../context/AuthContext'
import { useNavigate } from 'react-router-dom'

/*

Description of this page

profile pic
date of birth - ***(birthday notification)

age

submit button

*/

const EditProfileComponent = () => {

  const navigate = useNavigate()

  const {curr_user, setCurr_user} = useContext(Auth)
  const [file1, setFile1] = useState();
  const [file2, setFile2] = useState()
  
  const [showEditProfile, setShowEditProfile] = useState(true)

  useEffect(() => {
    const data = window.localStorage.getItem('Socially_Current_User')
    if(data !== null) setCurr_user(JSON.parse(data))
  }, [])

  const [userAddInfo, setUserAddInfo] = useState({
      age: 0,
      dateOfBirth: "",
      profilePic: "",
      coverPic: ""
  })

  const handleChange = (e) => {
    setUserAddInfo({
         ...userAddInfo,
         [e.target.name]: e.target.value
    })
  }

  const handleUpdate = async (e) => {
    e.preventDefault();

  
        const data1 = new FormData();
        const fileName1 = Date.now() + file1.name;
        data1.append("name", fileName1);
        data1.append("file", file1);

        try {
          await axios.post("http://localhost:9002/api/fileUpload", data1)
        } catch (err) {
          console.log(err)
        }

        const data2 = new FormData();
        const fileName2 = Date.now() + file2.name;
        data2.append("name", fileName2);
        data2.append("file", file2);

        try {
          await axios.post("http://localhost:9002/api/fileUpload", data2)
        } catch (err) {

        }

        const obj = {
          id: curr_user._id,
          age: userAddInfo.age,
          dateOfBirth: userAddInfo.dateOfBirth,
          profilePic: fileName1,
          coverPic: fileName2,
        }

        await axios.post("http://localhost:9002/api/addInfo", obj)
        .then(res => alert(res.data.message))


        const obj1 =curr_user;

        obj1.profilePic=fileName1;
        obj1.coverPic=fileName2;
        obj1.age=userAddInfo.age;
        obj1.dateOfBirth=userAddInfo.dateOfBirth;
        


        window.localStorage.removeItem("Socially_Current_User");
        window.localStorage.setItem("Socially_Current_User", JSON.stringify(obj1));





        setShowEditProfile(false)
        console.log(curr_user)
        setCurr_user(obj1);
        window.location.reload()
  }

  return (
      <>
        {
          (showEditProfile
          &&
          <div className='edit-profile'>
            <form>
              <div className="container">
                <h1>Complete your Profile</h1>
                <div className="content">
                    <div className="input-field">
                        <label>Profile photo</label>
                        <input type="file"
                              id="file1"
                              accept=".png,.jpeg,.jpg"
                              required
                              onChange={(e) => setFile1(e.target.files[0])}
                        />
                    </div>

                    <div className="input-field">
                        <label>Cover photo</label>
                        <input type="file"
                              id="file2"
                              accept=".png,.jpeg,.jpg"
                              required
                              onChange={(e) => setFile2(e.target.files[0])}
                        />
                    </div>

                    <div className="input-field">
                        <input type="number"
                              placeholder='Enter your age' 
                              name="age" 
                              value={userAddInfo.email}
                              onChange={handleChange}
                              required
                        />
                    </div>
                    <div className="input-field">
                        <input type="date" 
                              required
                              name="dateOfBirth" 
                              value={userAddInfo.dateOfBirth}
                              onChange={handleChange}
                        />
                    </div>
                    <div className="input-field">
                        <input type="submit"
                              value="Update"
                              onClick={handleUpdate}
                              style={{cursor: 'pointer'}}
                        />
                    </div>
                </div>
              </div>
            </form>
          </div>  

          )
        }
      </>
  )
}

export default EditProfileComponent
