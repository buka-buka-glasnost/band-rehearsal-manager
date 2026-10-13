import Link from "next/link";

export default function Home() {
    return (
        <div>
            <h1>Band Rehearsal Manager</h1>
            <p>Manage your label's bands, musicians and rehearsals.</p>

            <nav>
                <ul>
                    <li><Link href="/bands">Bands</Link></li>
                    <li><Link href="/musicians">Musicians</Link></li>
                    <li><Link href="/rehearsals">Rehearsals</Link></li>
                    <li><Link href="/attendances">Attendances</Link></li>
                </ul>
            </nav>
        </div>
    );
}