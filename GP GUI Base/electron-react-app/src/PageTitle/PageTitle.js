import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "./PageTitle.css";

export function PageTitle({ title, activeLink }) {
  return (
    <div
      className="PageTitle"
      style={{
        color:
          activeLink === "Home" || activeLink === "About Us"|| activeLink === "Source Code"
            ? "white"
            : "#092635",
      }}
    >
      {title}
    </div>
  );
}
