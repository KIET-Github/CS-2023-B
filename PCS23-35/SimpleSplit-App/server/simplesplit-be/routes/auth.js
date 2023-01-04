const router = require("express").Router();
const authController = require("../controllers/auth");
const isAuth = require("../middleware/is-auth");
const helper = require("../helpers/helper");

// Here
router.post("/login", helper.validateLogin, authController.login);
router.post("/signup", helper.validateSignup, authController.register);

module.exports = router;
