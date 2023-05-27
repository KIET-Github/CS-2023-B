const router = require("express").Router();
const User = require('../models/User')

router.post("/1", (req, res) => {

    try{
        const {currUserId ,requestUserId} = req.body

        User.findOneAndUpdate(
            {_id: currUserId},
            {
                $addToSet: {
                    friends: requestUserId,
                }
            },
            (err, user) => {
                res.send({message: "request sent successfully", res_user: user})
                console.log(err)
            }
        )
    } catch(err) {
        console.log(err)
    }
})

router.post("/2", (req, res) => {

    try{
        const {currUserId ,requestUserId} = req.body
        User.findOneAndUpdate(
            {_id: currUserId},
            {
                $pull: {
                    requests: requestUserId,
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