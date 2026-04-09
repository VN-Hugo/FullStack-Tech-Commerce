import { useState } from "react";
// import axios from "axios";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // const handleLogin = async (e) => {
  //   e.preventDefault();
  //   try {
  //     const res = await axios.post("http://localhost:8080/api/auth/login", {
  //       email,
  //       password,
  //     });
  //     console.log(res.data);
  //     alert("Login thành công");
  //   } catch (err) {
  //     alert("Sai tài khoản hoặc mật khẩu");
  //   }
  // };

  // const handleGoogleLogin = () => {
  //   window.location.href = "http://localhost:8080/oauth2/authorization/google";
  // };

  // const handleFacebookLogin = () => {
  //   window.location.href = "http://localhost:8080/oauth2/authorization/facebook";
  // };

  return (
    <div style={styles.container}>
      <form onSubmit={handleLogin} style={styles.form}>
        <h2>Login</h2>

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          style={styles.input}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          style={styles.input}
        />

        <button type="submit" style={styles.button}>
          Login
        </button>

        <hr />

        <button
          type="button"
          onClick={handleGoogleLogin}
          style={{ ...styles.button, backgroundColor: "#db4437" }}
        >
          Login with Google
        </button>

        <button
          type="button"
          onClick={handleFacebookLogin}
          style={{ ...styles.button, backgroundColor: "#4267B2" }}
        >
          Login with Facebook
        </button>
      </form>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "100vh",
    background: "#f5f5f5",
  },
  form: {
    background: "#fff",
    padding: "30px",
    borderRadius: "10px",
    width: "300px",
    boxShadow: "0 0 10px rgba(0,0,0,0.1)",
  },
  input: {
    width: "100%",
    padding: "10px",
    margin: "10px 0",
  },
  button: {
    width: "100%",
    padding: "10px",
    marginTop: "10px",
    border: "none",
    color: "#fff",
    backgroundColor: "#333",
    cursor: "pointer",
  },
};