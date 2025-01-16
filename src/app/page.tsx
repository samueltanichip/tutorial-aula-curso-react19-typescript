import Menu from "@/components/Menu";
import Footer from "@/components/Footer";
import User from "@/components/User";

const Home = () => {

  const userName = "Cesar";

  return (
    <div>
      <Menu /><br />

      <User name={userName}>
        <p>Este é um conteúdo extra fornecido como children.</p>
      </User><br />

      <h2>Bem-vindo Celke!</h2><br />
      <Footer />
    </div>
  );
}

export default Home;
