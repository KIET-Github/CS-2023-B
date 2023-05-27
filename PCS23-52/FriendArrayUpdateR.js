const router = require("express").Router();
const User = require('../models/User')

router.post("/", (req, res) => {

    try {
        const {currUserId ,requestUserId} = req.body
        User.findOneAndUpdate(
            {_id: requestUserId},
            {
                $addToSet: {
                    friends: currUserId,
                }
            },
            (err, user) => {
                res.send({message: "request sent successfully", res_user: user})
                console.log(err)
            }
        )
    } catch(err) {
        res.json(err)
    }

})

module.exports = router