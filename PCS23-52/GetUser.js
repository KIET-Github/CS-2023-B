const router = require("express").Router();
const User = require('../models/User')

router.post("/", (req, res) => {
    // res.send("My Api login")

    const {id} = req.body
    // console.log("ye meri id hai",id)

    User.findOne({_id: id}, (err, user) => {
        if(user) {
            res.send({message: "get user Successfully", res_user: user})
            // console.log(err)
            // console.log(user)
            // console.log("get user Successfully")
        } else {
            res.send({message: err, res_user: {}})
        }

    })

})

module.exports = router