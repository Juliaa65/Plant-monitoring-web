import React, { Component } from "react";
import { Navigate } from "react-router-dom";
import AuthService from "../services/auth.service";

export default class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = {
      redirect: null,
      userReady: false,
      currentUser: { email: "", firstName: "", lastName: "" }
    };
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser) {
      this.setState({ redirect: "/home" });
    } else {
      const { username, email } = currentUser;
      const [firstName, lastName] = username.split(' ');
      this.setState({ currentUser: { email, firstName, lastName }, userReady: true });
    }
  }

  render() {
    if (this.state.redirect) {
      return <Navigate to={this.state.redirect} />;
    }

    const { currentUser } = this.state;

    return (
        <div className="container">
          {this.state.userReady && (
              <div>
                <header className="jumbotron">
                  <h3>
                    Profile
                  </h3>
                </header>
                <p>
                  <strong>First Name:</strong> {currentUser.firstName}
                </p>
                <p>
                  <strong>Last Name:</strong> {currentUser.lastName}
                </p>
                <p>
                  <strong>Email:</strong> {currentUser.email}
                </p>
              </div>
          )}
        </div>
    );
  }
}
