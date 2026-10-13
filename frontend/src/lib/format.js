// "2026-05-15" -> "Fri, 15 May 2026". The date is built in local time so it never shifts a day.
export function formatDate(isoDate) {
    if (!isoDate) return "";
    const [year, month, day] = isoDate.split("-").map(Number);
    return new Intl.DateTimeFormat("en-GB", {
        weekday: "short",
        day: "numeric",
        month: "short",
        year: "numeric",
    }).format(new Date(year, month - 1, day));
}

// 150 -> "2 h 30 min"
export function formatDuration(minutes) {
    if (minutes == null) return "";
    const hours = Math.floor(minutes / 60);
    const rest = minutes % 60;
    if (hours === 0) return `${rest} min`;
    return rest === 0 ? `${hours} h` : `${hours} h ${rest} min`;
}

// Today's date as "YYYY-MM-DD" in local time, comparable with the dates from the API
export function todayIso() {
    const now = new Date();
    const pad = (n) => String(n).padStart(2, "0");
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}`;
}

// Form inputs give strings; the API expects numbers or null
export function toNumberOrNull(value) {
    return value === "" || value == null ? null : Number(value);
}

// countLabel(1, "band") -> "1 band", countLabel(3, "band") -> "3 bands"
export function countLabel(count, singular, plural = `${singular}s`) {
    return `${count} ${count === 1 ? singular : plural}`;
}
