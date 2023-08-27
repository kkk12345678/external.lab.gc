import React from "react";
import Login from "./pages/login";
import Certificates from "./pages/certificates";
import {BrowserRouter, Routes, Route, Link} from "react-router-dom";

export default function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="" element={<Link to="login">Login</Link>}></Route>
          <Route path="login" element={<Login />}></Route>
          <Route path="certificates" element={<Certificates />}></Route>
        </Routes>
      </BrowserRouter>
  );
}

