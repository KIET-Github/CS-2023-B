const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
require("dotenv").config();
const app = express();
const PORT = process.env.PORT || 8888;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Importing Routes
const serviceRoutes = require("./routes/service");
const authRoutes = require("./routes/auth");
const paymentRoutes = require("./routes/payment")
// To remove CORS (cross-resource-origin-platform) problem
app.use(cors());
// Connecting to database
const db = require("./db");
db.connectMongo();
// const Job = require("./models/Job");

// Routes
app.use(serviceRoutes);
app.use(authRoutes);
app.use(paymentRoutes);

app.listen(PORT, () => {
  console.log(`Listening to port ${PORT}`);
});
