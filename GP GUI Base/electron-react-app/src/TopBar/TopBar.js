import "bootstrap/dist/css/bootstrap.min.css";
import "./TopBar.css";
import { Link } from "react-router-dom";

export function Header({ visibleHyperlinks, activeLink }) {
  const allHyperlinks = [
    { url: "/", label: "Home" },
    { url: "/ImportProject", label: "Import Project" },
    { url: "/TraceLinks", label: "Proceed With" },
    { url: "/MaintainabilityScore", label: "Maintainability Scores" },
    { url: "/TraceLinks", label: "Trace Links" },
    { url: "/ViewSource", label: "Source Code" },
    { url: "/FilesDisplay", label: "FilesDisplay"},
    { url: "/AboutUs", label: "About Us" },
  ];

  const filteredHyperlinks = allHyperlinks.filter((link) =>
    visibleHyperlinks.includes(link.label)
  );

  const iconSource =
    activeLink === "Home" || activeLink === "About Us" || activeLink === "Source Code"
      ? require("../assets/searching1.png")
      : require("../assets/searching.png");

  return (
    <header className="header">
      <div className="container">
        <div className="row d-flex justify-content-between align-items-center">
          <div className="col-auto d-flex align-items-center">
            <img src={iconSource} alt="Icon" />
            <div className="LOGONAME" style={{
                  color:
                    activeLink === "Home" || activeLink === "About Us" || activeLink === "Source Code"
                      ? "white"
                      : "#092635"
                 
                }}>INTELLITEST</div>
          </div>
          <div className="col d-flex justify-content-end">
            {filteredHyperlinks.map((link) => (
              <Link
                className={`hyperpages mx-2`}
                key={link.label}
                to={link.url}
                style={{
                  color:
                    activeLink === "Home" || activeLink === "About Us" || activeLink === "Source Code"
                      ? "white"
                      : "#092635",
                  textDecoration: activeLink === link.label ? "underline" : "none",
                }}
              >
                {link.label}
              </Link>
            ))}
          </div>
        </div>
        <hr className="header-line"  style={{
                  backgroundColor:
                    activeLink === "Home" || activeLink === "About Us" || activeLink === "Source Code"
                      ? "none"
                      : "#092635",
                 
                }} />
      </div>
    </header>
  );
}
