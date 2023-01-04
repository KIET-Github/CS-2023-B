const router = require("express").Router();
const User = require('../models/User')

router.post("/", (req, res) => {
    const {user_id, friend_id, requests} = req.body

    User.findOneAndUpdate(
        {_id: friend_id},
        {
            $addToSet: {
                requests: user_id,
            }
        }, (err, user) => {
            res.send({message: "request sent successfully", res_user: user})
            // console.log(user)
        }
    )
})

module.exports = router