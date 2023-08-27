import React from "react";
import "../css/login.css";
import axios from "axios";

export default function Login() {
    const handleSubmit = (event) => {
        event.preventDefault();

        const {name, password} = document.forms[0];

        axios.post(
            "http://localhost:8080/users/login",
            {"name": name.value, "password": password.value},
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            })
            .then((res) => window.localStorage.setItem("jwt", res.data))
            .then(() => window.location.replace('/certificates'),
            (error) => {
                console.log(error);
                document.getElementById("message-error").style.display = "flex";
            }
        );


    };

    return (
        <div className="login">
            <div className="login-form">
                <div className="title">Sign In</div>
                <div className="form">
                    <form onSubmit={handleSubmit}>
                        <div className="input-container">
                            <label>Username </label>
                            <input type="text" name="name" required />
                        </div>
                        <div className="input-container">
                            <label>Password </label>
                            <input type="password" name="password" required />
                        </div>
                        <div className="error" id="message-error">Invalid login or password!</div>
                        <div className="button-container">
                            <input type="submit" value="Submit"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

