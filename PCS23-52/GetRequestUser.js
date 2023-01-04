const router = require("express").Router();
const User = require('../models/User')

router.post("/", async (req, res) => {

    try {
        // console.log("meri id", req.body.id)
        const user = await User.findById(req.body.id)
        const requestUsers = await Promise.all(
            user.requests.map((requestId) => {
                return User.findById(requestId)
            })
        )
        res.send({message: "request users fetched success", requestUsers: requestUsers})
        // console.log("ayaa")
    } catch(err) {
        res.json(err)
        // console.log("nhi aaya")
    }

})

module.exports = router