import React from 'react';
import Landing from "../screens/Landing";
import { Helmet } from "react-helmet";

function Home({setNavigation}) {
	return (
	<div>
    <Helmet>
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link href="https://fonts.googleapis.com/css2?family=Khula:wght@400;600;800&display=swap" rel="stylesheet" />
    </Helmet>
    <Landing setNavigation={setNavigation}/>
	</div>
	);
}

export default Home;