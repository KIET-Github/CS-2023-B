const router = require("express").Router();
const User = require('../models/User')

router.post("/", (req, res) => {
    const {id, age, dateOfBirth, profilePic, coverPic} = req.body

    User.findOneAndUpdate({_id: id},
        { $set: 
            { age: age, 
              dateOfBirth: dateOfBirth, 
              profilePic: profilePic, 
              coverPic: coverPic,
            }}, 
        (err, user) => {
            res.send({message: "profile updated successs"})
        })
})

module.exports = router