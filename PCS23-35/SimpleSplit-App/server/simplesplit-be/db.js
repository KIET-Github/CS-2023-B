const mongoose = require("mongoose");
const dbURI = process.env.DB_URI;

function connectMongo() {
  try {
    // var mongouri = dbURI;
    var mongouri = dbURI;
    
    mongoose.connect(
      mongouri,
      {
        useNewUrlParser: true,
        useUnifiedTopology: true,
        useFindAndModify: false,
      },
      (err) => {
        if (err) console.log(err);
        else console.log("Connected to MongoDB");
      }
    );
  } catch (e) {
    console.log(e);
  }
}

module.exports = { connectMongo };
