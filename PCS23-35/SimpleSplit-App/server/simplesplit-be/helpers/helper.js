const bcrypt = require("bcrypt");
const userSchema = require("../models/User");

// VALIDATING SIGNUP VALUES BEFORE SAVING IN BACKEND
exports.validateSignup = async (req, res, next) => {
  try {
    const { email, phone, password } = req.body;

    const emailRegex =
      /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (!emailRegex.test(email)) throw "Invalid Email Id";

    phoneRegex = /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im;
    if (!phoneRegex.test(phone)) throw "Invalid Phone Number";
    if (password.length < 6) throw "Password must be atleast 6 characters long";

    const checkUser = await userSchema.findOne({ email: email });
    if (!(checkUser === null)) throw "User already exists!";

    next();
  } catch (err) {
    return res.status(400).json({ success: false, message: err });
  }
};

// VALIDATING VALUES LOGGING IN
exports.validateLogin = async (req, res, next) => {
  try {
    const { email, password } = req.body;

    const user = await userSchema.findOne({ email: email });
    if (user === null) throw "User does not exist!";

    const result = await bcrypt.compare(password, user.password);
    if (result === false) throw "Invalid Password";

    next();
  } catch (err) {
    console.log(err);
    return res.status(400).json({ success: false, message: err });
  }
};
