import React, { Component } from "react";

export default class Home extends Component {
    render() {

        const htmlContent = `
      <div>
        <h1 style="color: #008000;">Welcome to Plant Care System</h1>
        <p style="color: #008000;">
          Our plant monitoring system helps you keep track of the health and growth of your plants.
        </p>
        <p style="color: #008000;">
          With real-time data and insights, you can ensure optimal conditions for your plants to thrive.
        </p>
        <p style="color: #008000;">
          Get started today and take your plant care to the next level!
        </p>
      </div>
    `;

        return (
            <div className="container">
                <header className="jumbotron">

                    <div dangerouslySetInnerHTML={{ __html: htmlContent }} />
                </header>
            </div>
        );
    }
}
