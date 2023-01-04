const router = require("express").Router();
const User = require('../models/User')

router.post("/", async (req, res) => {

    try {
        const user = await User.findById(req.body.id)
        const friendsArray = await Promise.all(
            user.friends.map((friendId) => {
                return User.findById(friendId)
            })
        )
        res.send({message: "request users fetched success", friends: friendsArray})
    } catch(err) {
        res.json(err)
    }

})

module.exports = router