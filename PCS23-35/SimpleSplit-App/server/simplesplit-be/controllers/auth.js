const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");

const userSchema = require("../models/User");

exports.register = async (req, res, next) => {
  try {
    const { name, phone, email, password } = req.body;

    const salt = await bcrypt.genSalt(8);
    const hashedPass = await bcrypt.hash(password, salt);

    const user = new userSchema({
      name: name,
      email: email,
      phone: phone,
      password: hashedPass,
    });
    await user.save();
    return res
      .status(201)
      .json({ success: true, message: "user registered successfully" });
  } catch (err) {
    console.log(err);
    return res.status(400).json({ success: false, message: err });
  }
};

exports.login = async (req, res, next) => {
  try {
    const { email } = req.body;

    const user = await userSchema.findOne({ email: email });
    //console.log(`user:${user}`);
    const token = jwt.sign(JSON.stringify(user), process.env.JWT_SECRET);
    //console.log(`token: ${token}`);
    res
      .status(200)
      .json({ success: true, message: "Logged in successfully", token: token });
  } catch (err) {
    console.log(err);
    return res.status(400).json({ success: false, message: err });
  }
};
