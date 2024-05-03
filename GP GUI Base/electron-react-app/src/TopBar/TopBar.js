import "bootstrap/dist/css/bootstrap.min.css";
import "./TopBar.css";
import { Link } from "react-router-dom";

export function Header({ visibleHyperlinks, activeLink }) {
  const allHyperlinks = [
    { url: "/", label: "Home" },
    { url: "/AboutUs", label: "About Us" },
    { url: "/ImportProject", label: "Import Project" },
    { url: "/ProceedWith", label: "Proceed With" },
    { url: "/MaintainabilityScore", label: "Maintainability Scores" },
    { url: "/TraceLinks", label: "Trace Links" },
    { url: "/ViewSource", label: "Source Code" },


  ];

  const filteredHyperlinks = allHyperlinks.filter((link) =>
    visibleHyperlinks.includes(link.label)
  );

  const iconSource =
    activeLink === "Home" || activeLink === "About Us"||activeLink === "Source Code"
      ? require("../assets/searching1.png")
      : require("../assets/searching.png");

  return (
    <header className="header">
      <div className="container">
        <div className="row d-flex justify-content-center align-items-center">
          <div className="col-auto">
            <img src={iconSource} alt="Icon" />
          </div>
          <div className="col-md-10 d-flex justify-content-center align-items-center">
            {filteredHyperlinks.map((link) => (
              <Link
                className={`hyperpages mx-5`}
                key={link.label}
                to={link.url}
                style={{
                  color:
                    activeLink === "Home" || activeLink === "About Us"||activeLink === "Source Code"
                      ? "white"
                      : "#092635",
                  textDecoration:
                    activeLink === link.label ? "underline" : "none",
                }}
              >
                {link.label}
              </Link>
            ))}
          </div>
        </div>
      </div>
    </header>
  );
}
