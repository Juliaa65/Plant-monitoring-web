import React, { Component } from "react";
import { Routes, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AuthService from "./services/auth.service";

import Login from "./components/login.component";
import Register from "./components/register.component";
import Home from "./components/home.component";
import Profile from "./components/profile.component";
import BoardUser from "./components/board-user.component";
import CreatePlant from "./components/new-plant.component";
import PlantInfo from "./components/plant-info.component";
import EditPlant from "./components/edit-plant.component";
import PhotoGallery from "./components/gallery.component";
import Dashboard from "./components/dashboard.component";
import Journal from "./components/journal.component";


import EventBus from "./common/EventBus";




class App extends Component {
  constructor(props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      showModeratorBoard: false,
      showAdminBoard: false,
      currentUser: undefined,
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();

    if (user) {
      this.setState({
        currentUser: user,
      });
    }
    
    EventBus.on("logout", () => {
      this.logOut();
    });
  }

  componentWillUnmount() {
    EventBus.remove("logout");
  }

  logOut() {
    AuthService.logout();
    this.setState({
      showModeratorBoard: false,
      showAdminBoard: false,
      currentUser: undefined,
    });
  }

  render() {
    const { currentUser} = this.state;

    return (
        <div>
          <nav className="navbar navbar-expand navbar-dark bg-green">
            <Link to={"/"} className="navbar-brand">
              Plant-Care
            </Link>
            <div className="navbar-nav mr-auto">
              <li className="nav-item">
                <Link to={"/home"} className="nav-link">
                  Home
                </Link>
              </li>


              {currentUser && (
                  <li className="nav-item">
                    <Link to={"/user"} className="nav-link">
                      Рослини
                    </Link>
                  </li>
              )}
            </div>

            {currentUser ? (
                <div className="navbar-nav ml-auto">
                  <li className="nav-item">
                    <Link to={"/profile"} className="nav-link">
                      {currentUser.username}
                    </Link>
                  </li>
                  <li className="nav-item">
                    <a href="/login" className="nav-link" onClick={this.logOut}>
                      Вихід
                    </a>
                  </li>
                </div>
            ) : (
                <div className="navbar-nav ml-auto">
                  <li className="nav-item">
                    <Link to={"/login"} className="nav-link">
                      Вхід
                    </Link>
                  </li>

                  <li className="nav-item">
                    <Link to={"/register"} className="nav-link">
                      Реєстрація
                    </Link>
                  </li>
                </div>
            )}
          </nav>

          <div className="container mt-3">
            <Routes>
              <Route path="/" element={<Home/>}/>
              <Route path="/home" element={<Home/>}/>
              <Route path="/login" element={<Login/>}/>
              <Route path="/register" element={<Register/>}/>
              <Route path="/profile" element={<Profile/>}/>
              <Route path="/user" element={<BoardUser/>}/>
              <Route path="/new-plant" element={<CreatePlant/>} />
              <Route path="/plant-info/:plantId" element={<PlantInfo/>}/>
              <Route path="/plant-edit/:plantId" element={<EditPlant/>}/>
              <Route path="/gallery/:plantId" element={<PhotoGallery/>}/>
              <Route path="/dashboard/:plantId" element={<Dashboard/>}/>
              <Route path="/journal/:plantId" element={<Journal/>}/>
            </Routes>
          </div>


        </div>
    );
  }
}

export default App;
