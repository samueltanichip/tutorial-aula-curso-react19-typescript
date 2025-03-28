import Link from 'next/link';

const Menu = () => {
    return (
        <nav>
            <ul>
                <li>
                    <Link href="/">Home</Link>
                </li>
                <li>
                    <Link href="/about">Sobre a Empresa</Link>
                </li>
                <li>
                    <Link href="/contact">Contato</Link>
                </li>
            </ul>
        </nav>
    )
}

export default Menu;