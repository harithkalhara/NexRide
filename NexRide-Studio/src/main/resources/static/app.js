function icon(name) {
  const paths = {
    arrow: "M5 12h14m-6-6 6 6-6 6",
    pin: "M20 10c0 6-8 12-8 12S4 16 4 10a8 8 0 1 1 16 0ZM12 7v6m-3-3h6",
    grid: "M3 3h7v7H3zM14 3h7v7h-7zM3 14h7v7H3zM14 14h7v7h-7z",
    bike: "M7 16a4 4 0 1 1-8 0 4 4 0 0 1 8 0Zm16 0a4 4 0 1 1-8 0 4 4 0 0 1 8 0ZM3 16l5-9h7l4 9M7 16h8L8 4H5m9 0h4",
    clock: "M12 8v5l3 2M22 12a10 10 0 1 1-20 0 10 10 0 0 1 20 0Z",
    shield: "M12 2 3 6v7c0 5 9 9 9 9s9-4 9-9V6l-9-4Zm-4 9 3 3 5-6",
    users:
      "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2m20 0v-2a4 4 0 0 0-3-3.87M13 3a4 4 0 0 1 0 8M13 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0Z",
    wallet: "M3 5h17v15H3V5Zm0 0 14-3v3m-2 8h5",
    help: "M9 9a3 3 0 1 1 5 2c-2 1-2 2-2 3m0 3h.01M22 12a10 10 0 1 1-20 0 10 10 0 0 1 20 0Z",
    settings:
      "M12 8a4 4 0 1 1 0 8 4 4 0 0 1 0-8Zm0-6v3m0 14v3M2 12h3m14 0h3M5 5l2 2m10 10 2 2M5 19l2-2M17 7l2-2",
    user: "M20 21v-2a6 6 0 0 0-6-6h-4a6 6 0 0 0-6 6v2M16 6a4 4 0 1 1-8 0 4 4 0 0 1 8 0Z",
    calendar: "M4 5h16v17H4V5Zm0 5h16M8 2v6m8-6v6",
    search: "M21 21l-5-5M18 10a8 8 0 1 1-16 0 8 8 0 0 1 16 0Z",
  };
  return `<svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="${paths[name] || paths.arrow}"/></svg>`;
}
function brand() {
  return `<a class="brand" href="/" aria-label="NexRide home"><img src="${esc(site.logoUrl || "/assets/nexride-mark.svg")}" alt=""><span>NexRide<span class="brand-dot">.</span></span></a>`;
}
function themeNav() {
  return `<div class="announcement">A little less ordinary. A lot more Sri Lanka. <a href="/#how">Plan your first ride ${icon("arrow")}</a></div><header class="topbar"><div class="container navwrap">${brand()}<nav class="navlinks" id="navlinks" aria-label="Main navigation"><a href="/#fleet">Find a ride</a><a href="/#how">How it works</a><a href="/#rides-list">Ride together</a><a href="/#contact">Contact</a><a href="/login">User sign in</a></nav><div class="navright">${themeButton()}<select class="currency" aria-label="Display currency" data-currency><option value="LKR" ${currency === "LKR" ? "selected" : ""}>LKR</option><option value="USD" ${currency === "USD" ? "selected" : ""}>USD</option></select>${me ? `<a class="btn dark small" href="${me.role === "ADMIN" ? "/admin#overview" : "/#account"}">My dashboard ${icon("arrow")}</a>` : `<a class="text-btn nav-signin" href="/login">Sign in</a><a class="btn dark small" href="/#register">Get started ${icon("arrow")}</a>`}<button class="icon-btn menu-btn" data-action="menu" aria-label="Toggle navigation" aria-expanded="false">☰</button></div></div></header>`;
}
function footer() {
  return `<footer class="footer"><div class="container"><div class="footer-grid"><div>${brand()}<p>More freedom. More little discoveries.<br>Your next chapter starts on two wheels.</p><div class="footer-tag">Made for the open road ↗</div></div><div><h3>Explore</h3><a href="/#fleet">Motorcycles & bicycles</a><a href="/#rides-list">Community rides</a><a href="/#how">How it works</a></div><div><h3>Your NexRide</h3><a href="/#account">My dashboard</a><a href="/#billing">Invoices & payments</a><a href="/#support">Help & support</a></div><div><h3>Let's talk</h3>${site.contactEmail ? `<a href="mailto:${esc(site.contactEmail)}">${esc(site.contactEmail)}</a>` : '<a href="/#contact">Contact the team</a>'}${site.contactPhone ? `<a href="tel:${esc(site.contactPhone.replace(/[^+0-9]/g, ""))}">${esc(site.contactPhone)}</a>` : ""}<a href="/admin">Admin sign in</a></div></div><div class="footer-bottom"><span>© ${new Date().getFullYear()} NexRide. All rights reserved.</span><span class="release-badge">ATLAS / 04</span><span>Sri Lanka · LKR / USD</span></div></div></footer>`;
}
function searchForm() {
  return `<form class="search-inner" id="search-form"><div class="search-field"><label for="q-city">${icon("pin")} Pick-up city</label><input id="q-city" name="city" list="cities" placeholder="Where are you going?" value="${esc(filters.city)}"><datalist id="cities">${[...new Set(fleet.map((b) => b.location))].map((c) => `<option value="${esc(c)}">`).join("")}</datalist></div><div class="search-field"><label for="q-kind">${icon("bike")} Your wheels</label><select id="q-kind" name="kind"><option value="">All rides</option><option value="MOTORCYCLE" ${filters.kind === "MOTORCYCLE" ? "selected" : ""}>Motorcycles</option><option value="BICYCLE" ${filters.kind === "BICYCLE" ? "selected" : ""}>Bicycles</option></select></div><div class="search-field"><label for="q-start">${icon("calendar")} Pick-up</label><input id="q-start" name="start" type="date" min="${today()}" value="${esc(filters.start)}"></div><div class="search-field"><label for="q-end">${icon("calendar")} Return</label><input id="q-end" name="end" type="date" min="${filters.start || today()}" value="${esc(filters.end)}"></div><button class="btn" type="submit">Find a ride ${icon("search")}</button><div class="error-box search-error" aria-live="polite"></div></form>`;
}
function photo(b) {
  return `<div class="vehicle-img"><img src="${esc(b.photoUrl || "/assets/motorcycle.jpg")}" alt="${esc(b.samplePhoto ? "Reference image for " + b.name : b.name)}" loading="lazy"><span class="pill">${b.kind === "BICYCLE" ? "Bicycle" : "Motorcycle"}</span>${b.samplePhoto ? '<span class="sample-label">Reference photo</span>' : ""}</div>`;
}
function card(b) {
  let rs = reviews.filter((r) => r.vehicleId === b.id);
  return `<article class="vehicle-card reveal">${photo(b)}<div class="vehicle-body"><div class="vehicle-location">${icon("pin")}${esc(b.location)}<span>${rs.length ? "★ " + (rs.reduce((s, r) => s + r.rating, 0) / rs.length).toFixed(1) + " (" + rs.length + ")" : "New listing"}</span></div><h3>${esc(b.name)}</h3><div class="vehicle-meta"><span>${icon("bike")}${b.kind === "BICYCLE" ? esc(b.gearCount || 1) + " gears" : esc(b.engineCc) + " cc"}</span><span>${icon("calendar")} Daily rental</span></div><div class="vehicle-bottom"><div class="vehicle-price"><b>${money(b.dailyRate)}</b><small> / day</small></div>${adminMode && me?.role === "ADMIN" ? `${btn("Edit", "bike-edit", b.id, "dark small")}${btn("Delete", "bike-delete", b.id, "text-btn small")}` : btn("View ride " + icon("arrow"), "bike", b.id, "dark small")}</div>${adminMode ? status(b.status) : ""}</div></article>`;
}
function hero() {
  return `<main id="main"><section class="studio-hero container atlas-hero"><div class="hero-copy-block"><div class="eyebrow"><span class="live-dot"></span> THE NEXT TURN IS YOURS</div><h1>Your island.<br><em>Unscripted.</em></h1><p>Coastal roads. Coffee stops. A turn you didn't plan.<br>Find your motorcycle or bicycle and make the day yours.</p><div class="hero-actions"><a class="btn" href="/#fleet">Find your ride ${icon("arrow")}</a><a class="text-btn" href="/#how">How NexRide works ↗</a></div><div class="hero-footnote"><span class="outline-round">${icon("bike")}</span><span>Two wheels. A different perspective.<br><strong>Motorcycles & bicycles, all in one place.</strong></span></div><span class="hero-index">01 / GO SOMEWHERE GOOD</span></div><div class="hero-visual"><img class="hero-main-photo" src="/assets/rider.jpg" alt="Motorcyclist taking a quiet break with a classic bike" fetchpriority="high"><div class="photo-topline"><span class="hero-pill">EXPLORE • SRI LANKA</span><span>THE LONG WAY HOME</span><span>↗</span></div><div class="photo-caption"><span>Less itinerary.<br><b>More possibility.</b></span><a href="/#fleet" aria-label="Explore rentals">↗</a></div><div class="floating-note">${icon("pin")} <span>Start local.<br><b>Go a little further.</b></span></div></div></section><div class="container searchbar">${searchForm()}</div><div class="container trust-strip"><span>${icon("calendar")} Choose your dates</span><span>${icon("wallet")} Clear daily prices</span><span>${icon("users")} Shared local journeys</span><span>${icon("help")} Support from your account</span></div><section class="section" id="fleet"><div class="container"><div class="section-head"><div><div class="eyebrow">FIND YOUR KIND OF FREEDOM</div><h2>Meet your next ride.</h2><p>From a morning pedal to a weekend on the coast.</p></div><a class="text-btn" href="/#fleet">Explore all rides ${icon("arrow")}</a></div><div class="filter-row"><button class="filter-chip active" data-kind="">All rides</button><button class="filter-chip" data-kind="MOTORCYCLE">Motorcycles</button><button class="filter-chip" data-kind="BICYCLE">Bicycles</button><span class="result-count">${fleet.length} listings to explore</span></div><div class="fleet-grid">${fleet.slice(0, 6).map(card).join("") || empty("Your next ride is on its way", "The team is preparing the fleet. Check back soon.")}</div></div></section><section class="container category-story"><div class="story-image"><img src="/assets/cycling.jpg" alt="Cyclist preparing a road bicycle" loading="lazy"><span>THE SLOWER SIDE OF LIFE</span></div><div class="story-copy"><div class="eyebrow">NO ENGINE. ALL EXPERIENCE.</div><h2>A little pedal.<br>A fresh perspective.</h2><p>Turn a familiar street into a new discovery. Find a bicycle for a city wander, a gentle morning or an unhurried afternoon.</p><button class="btn dark" data-kind="BICYCLE">Explore bicycles ${icon("arrow")}</button><div class="story-small">Take your time. That's the whole idea.</div></div></section><section class="section" id="how"><div class="container"><div class="section-head"><div><div class="eyebrow">LESS ADMIN. MORE ADVENTURE.</div><h2>From browsing to riding.</h2></div><p class="section-aside">A simple process, with a real team<br>confirming your booking.</p></div><div class="steps">${[
    [
      "01",
      "Choose your wheels",
      "Compare bikes, locations and daily rates. Pick the ride that fits your plans.",
      "search",
    ],
    [
      "02",
      "Request your dates",
      "Send a rental request. Track the team’s approval in your dashboard.",
      "calendar",
    ],
    [
      "03",
      "Pay & pick up",
      "Pay your approved invoice, check the bike with the team and start exploring.",
      "bike",
    ],
  ]
    .map(
      (x) =>
        `<article class="step reveal"><div class="step-top">${icon(x[3])}<span>${x[0]}</span></div><h3>${x[1]}</h3><p>${x[2]}</p></article>`,
    )
    .join(
      "",
    )}</div></div></section><section class="community-band"><div class="container community-inner"><div><div class="eyebrow">GOOD ROADS. BETTER COMPANY.</div><h2>Some journeys<br>are better shared.</h2><p>Find a community ride heading your way, or offer a passenger seat. Make the road a little less lonely.</p><a class="btn" href="/#rides-list">Meet your next journey ${icon("arrow")}</a></div><div class="community-preview"><div class="community-label">THE COMMUNITY BOARD</div>${rides.length ? rides.slice(0, 2).map(rideCard).join("") : `<div class="ride-invitation">${icon("users")}<h3>Be the start of something.</h3><p>Your first shared journey could bring someone else's plans to life.</p><a class="text-btn" href="/#rides">Offer a ride ↗</a></div>`}</div></div></section><section class="section"><div class="container faq-layout"><div><div class="eyebrow">BEFORE YOU SET OFF</div><h2>A few good<br>questions.</h2><p>Still wondering about something?<br><a class="text-btn" href="/#contact">Talk to the team ↗</a></p></div><div class="faq-list">${[
    [
      "Is my rental confirmed immediately?",
      "Your request goes to the NexRide team. A booking is confirmed only after approval; you can follow its status in your dashboard.",
    ],
    [
      "Can I rent a bicycle as well?",
      "Yes. Choose Bicycles in the collection to see locations, available listings and daily rates.",
    ],
    [
      "Can I view prices in US dollars?",
      "Yes, when an exchange rate is configured. Select USD in the header. The checkout shows your charge currency and amount; invoice accounting remains in LKR.",
    ],
    [
      "How do card payments work?",
      "Approved bookings receive an invoice. When online payments are enabled, Visa and Mastercard payments are completed on the payment provider’s secure page.",
    ],
    [
      "What should I check at pickup?",
      "Confirm the exact vehicle, its condition, pickup and return arrangements, and any riding-document requirements with the team before you leave.",
    ],
  ]
    .map(([q, a]) => `<details><summary>${q}<span>+</span></summary><p>${a}</p></details>`)
    .join(
      "",
    )}</div></div></section><section class="contact" id="contact"><div class="container contact-inner"><div><div class="eyebrow">WE'RE A MESSAGE AWAY</div><h2>Let's get you<br>on the road<span class="orange">.</span></h2><p>${esc(site.tagline || "Tell us what you have in mind. We’ll help with the next step.")}</p><a class="btn dark" href="${me ? "#support" : "#register"}">${me ? "Open a support request" : "Create your account"} ${icon("arrow")}</a></div><div class="contact-cards">${site.contactEmail ? `<div class="contact-item"><small>EMAIL THE TEAM</small><a href="mailto:${esc(site.contactEmail)}">${esc(site.contactEmail)}</a></div>` : '<div class="contact-item"><small>CONTACT THE TEAM</small><a href="/#support">Send a support request ↗</a></div>'}${site.contactPhone ? `<div class="contact-item"><small>GIVE US A CALL</small><a href="tel:${esc(site.contactPhone.replace(/[^+0-9]/g, ""))}">${esc(site.contactPhone)}</a></div>` : ""}${site.address ? `<div class="contact-item"><small>FIND US</small><strong>${esc(site.address)}</strong></div>` : ""}${site.hours ? `<div class="contact-item"><small>OPENING HOURS</small><strong>${esc(site.hours)}</strong></div>` : ""}<div class="socials">${site.instagramUrl ? `<a href="${esc(site.instagramUrl)}" target="_blank" rel="noopener">Instagram ↗</a>` : ""}${site.facebookUrl ? `<a href="${esc(site.facebookUrl)}" target="_blank" rel="noopener">Facebook ↗</a>` : ""}</div></div></div></section></main>`;
}
function empty(title, description) {
  return `<div class="empty">${icon("bike")}<h3>${esc(title)}</h3><p>${esc(description)}</p><a class="text-btn" href="/#fleet">Explore the fleet ↗</a></div>`;
}
function rideCard(r) {
  return `<article class="ride-card"><div class="ride-top"><span class="pill">Community ride</span><span>NO FARE</span></div><h3>${esc(r.origin)} ${icon("arrow")} ${esc(r.destination)}</h3><div class="vehicle-meta"><span>${icon("calendar")}${date(r.departure)}</span><span>${icon("users")}${r.seats} seats</span></div><p>${esc(r.description || "A new journey with the NexRide community.")}</p>${me ? btn("Request a seat " + icon("arrow"), "ride-join", r.id, "dark small") : '<a class="btn dark small" href="/#login">Sign in to join ↗</a>'}</article>`;
}
function fleetPage() {
  let sorted = [...fleet];
  if (filters.sort === "low") sorted.sort((a, b) => a.dailyRate - b.dailyRate);
  if (filters.sort === "high") sorted.sort((a, b) => b.dailyRate - a.dailyRate);
  return (
    themeNav() +
    `<main id="main" class="page-shell container"><div class="eyebrow">THE NEXRIDE COLLECTION</div><h1 class="page-title">Find your kind of freedom.</h1><p class="muted">Your dates. Your destination. Your next set of wheels.</p><div class="catalog-search">${searchForm()}</div><div class="section-head catalog-toolbar"><div class="filter-row">${[
      ["", "All rides"],
      ["MOTORCYCLE", "Motorcycles"],
      ["BICYCLE", "Bicycles"],
    ]
      .map(
        ([k, l]) =>
          `<button class="filter-chip ${filters.kind === k ? "active" : ""}" data-kind="${k}">${l}</button>`,
      )
      .join(
        "",
      )}</div><div><span class="result-count">${sorted.length} rides</span> <select aria-label="Sort rides" id="fleet-sort"><option value="">Recommended</option><option value="low" ${filters.sort === "low" ? "selected" : ""}>Price: low to high</option><option value="high" ${filters.sort === "high" ? "selected" : ""}>Price: high to low</option></select></div></div><div class="fleet-grid">${sorted.map(card).join("") || empty("No rides found", "Try a different city, type or date range.")}</div>${currencyNote()}</main>` +
    footer()
  );
}
function bikePage(b) {
  if (!b) return headerContent(empty("Ride not found", "This listing may no longer be available."));
  let rs = reviews.filter((r) => r.vehicleId === b.id);
  return (
    themeNav() +
    `<main id="main" class="page-shell container"><a class="text-btn" href="/#fleet">← Back to the collection</a><div class="detail-layout"><div><div class="detail-image"><img src="${esc(b.photoUrl || "/assets/motorcycle.jpg")}" alt="${esc(b.name)}"></div>${b.samplePhoto ? '<p class="field-help">Stock reference photo. Confirm the exact vehicle with the team before pickup.</p>' : ""}<section class="detail-about"><div class="eyebrow">MADE FOR YOUR NEXT CHAPTER</div><h2>A little more about this ride.</h2><p>${esc(b.description)}</p><div class="detail-specs"><div>${icon("pin")}<strong>${esc(b.location)}</strong><small>Pick-up city</small></div><div>${icon("bike")}<strong>${b.kind === "BICYCLE" ? esc(b.gearCount) + " gears" : esc(b.engineCc) + " cc"}</strong><small>${b.kind === "BICYCLE" ? "Bicycle" : "Motorcycle"}</small></div><div>${icon("clock")}<strong>7+ days</strong><small>10% rental discount</small></div></div></section><section class="panel"><div class="panel-head"><h2>From the riders</h2><span>${rs.length} reviews</span></div>${rs.map((r) => `<article class="review-row"><strong class="orange">${"★".repeat(r.rating)}${"☆".repeat(5 - r.rating)}</strong><p>${esc(r.comment)}</p></article>`).join("") || '<p class="muted">No reviews yet. Reviews are available after a completed rental.</p>'}${me ? btn("Write a review", "review", b.id, "dark small") : ""}</section></div><aside class="booking-panel"><div class="eyebrow">${esc(b.kind)} · ${esc(b.location)}</div><h1>${esc(b.name)}</h1><div class="detail-price">${money(b.dailyRate)} <span>/ day</span></div>${currencyNote()}<div class="notice">Request your dates. The team will confirm availability before payment.</div><div class="form-grid">${field("detailStart", "Pick-up", "date", filters.start || today(), `min="${today()}"`)}${field("detailEnd", "Return", "date", filters.end || filters.start || today(), `min="${today()}"`)}</div><div id="booking-estimate" class="estimate" data-rate="${b.dailyRate}"></div>${btn("Request this ride " + icon("arrow"), "request-rental", b.id, "full")}<p class="field-help">No payment is taken when you send a request.</p><hr><div class="vehicle-meta">${icon("shield")} Your booking stays in your dashboard.</div></aside></div></main>` +
    footer()
  );
}
function updateEstimate() {
  let out = $("#booking-estimate");
  if (!out) return;
  let s = $("#f-detailStart").value,
    e = $("#f-detailEnd").value,
    days = Math.round((new Date(e) - new Date(s)) / 86400000) + 1;
  if (!s || !e || days < 1) {
    out.textContent = "Choose a valid pick-up and return date.";
    return;
  }
  let total = Number(out.dataset.rate) * days * (days >= 7 ? 0.9 : 1);
  out.innerHTML = `<span>${days} day${days === 1 ? "" : "s"}${days >= 7 ? " · 10% off" : ""}</span><strong>${money(total)}</strong>`;
  filters.start = s;
  filters.end = e;
}

("use strict");

// Apply the saved/system theme from the external script so the page complies
// with the Content-Security-Policy (which intentionally blocks inline scripts).
try {
  document.documentElement.dataset.theme =
    localStorage.getItem("nexride-theme") ||
    (window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches
      ? "dark"
      : "light");
} catch (_) {
  document.documentElement.dataset.theme = "light";
}
const $ = (q, root = document) => root.querySelector(q),
  $$ = (q, root = document) => [...root.querySelectorAll(q)];
let me = null,
  csrf = "",
  site = {},
  fleet = [],
  reviews = [],
  rides = [],
  paymentOptions = { ready: false, mode: "disabled" },
  currency = localStorage.getItem("nexride-currency") || "LKR",
  filters = { kind: "", city: "", start: "", end: "" },
  adminMode = location.pathname.startsWith("/admin");
const esc = (v) =>
  String(v ?? "").replace(
    /[&<>"']/g,
    (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[c],
  );
const sym = (c) => (c === "USD" ? "$" : "Rs.");
function money(n, c = currency) {
  let rate = Number(site.lkrPerUsd || 0),
    value = Number(n || 0);
  if (c === "USD" && rate > 0)
    return `$${(value / rate).toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  return `Rs. ${Math.round(value).toLocaleString("en-LK")}`;
}
function date(v) {
  if (!v) return "—";
  try {
    return new Intl.DateTimeFormat("en", { dateStyle: "medium" }).format(
      new Date(v.includes("T") ? v : v + "T00:00:00"),
    );
  } catch {
    return esc(v);
  }
}
function today() {
  let d = new Date();
  return new Intl.DateTimeFormat("en-CA", { timeZone: "Asia/Colombo" }).format(d);
}
async function api(path, method = "GET", body = null) {
  let opt = { method, credentials: "same-origin", headers: {} };
  if (body instanceof FormData) opt.body = body;
  else if (body !== null) {
    opt.headers["Content-Type"] = "application/json";
    opt.body = JSON.stringify(body);
  }
  if (!["GET", "HEAD"].includes(method) && csrf) opt.headers["X-CSRF-Token"] = csrf;
  let r = await fetch("/api" + path, opt);
  let j = await r.json().catch(() => ({}));
  if (!r.ok) {
    if (r.status === 401 && me) {
      me = null;
      location.assign(adminMode ? "/admin#login" : "/#login");
    }
    throw Error(j.message || "Please try again.");
  }
  return j;
}
function btn(label, act, id = "", cls = "") {
  return `<button type="button" class="btn ${cls}" data-action="${esc(act)}" data-id="${esc(id)}">${label}</button>`;
}
function status(v) {
  return `<span class="status ${esc(v)}">${esc((v || "").replaceAll("_", " "))}</span>`;
}
function field(name, label, type = "text", value = "", attrs = "") {
  return `<div class="form-group"><label for="f-${esc(name)}">${esc(label)}</label><input id="f-${esc(name)}" name="${esc(name)}" type="${type}" value="${esc(value)}" ${attrs}></div>`;
}
function select(name, label, opts, value = "") {
  return `<div class="form-group"><label for="f-${esc(name)}">${esc(label)}</label><select id="f-${esc(name)}" name="${esc(name)}">${opts
    .map((o) => {
      let v = Array.isArray(o) ? o[0] : o,
        l = Array.isArray(o) ? o[1] : o;
      return `<option value="${esc(v)}" ${String(v) === String(value) ? "selected" : ""}>${esc(l)}</option>`;
    })
    .join("")}</select></div>`;
}
function area(name, label, value = "", rows = 4) {
  return `<div class="form-group"><label for="f-${esc(name)}">${esc(label)}</label><textarea id="f-${esc(name)}" name="${esc(name)}" rows="${rows}">${esc(value)}</textarea></div>`;
}
function err(msg = "") {
  let e = $("#modal").open ? $(".error-box", $("#modal")) : $(".error-box");
  if (e) e.textContent = msg;
}
function toast(msg) {
  let t = $("#toast");
  t.textContent = msg;
  t.className = "toast show";
  setTimeout(() => t.classList.remove("show"), 2600);
}
function setApp(html) {
  $("#app").innerHTML = html;
  if (!document.querySelector('[data-action="theme"]'))
    $("#app").insertAdjacentHTML(
      "afterbegin",
      '<div class="standalone-theme">' + themeButton() + "</div>",
    );
  syncThemeButtons();
  observeReveal();
}
function headerContent(route) {
  return themeNav() + `<div class="page-shell container" id="main">${route}</div>` + footer();
}
function rentalRows(items, admin = false) {
  return `<div class="table-wrap"><table class="table"><thead><tr><th>Vehicle</th><th>Dates</th><th>Total</th><th>Status</th><th>Action</th></tr></thead><tbody>${
    items
      .map((r) => {
        let b = fleet.find((x) => x.id === r.vehicleId);
        return `<tr><td>${esc(b?.name || "Vehicle")}</td><td>${date(r.startDate)} – ${date(r.endDate)}</td><td>${money(r.total)}</td><td>${status(r.status)}</td><td>${r.status === "PENDING" ? btn(admin ? "Approve" : "Cancel", admin ? "rental-approve" : "rental-cancel", r.id, "small dark") + (admin ? btn("Decline", "rental-reject", r.id, "small outline") : "") : r.status === "APPROVED" && admin ? btn("Confirm pickup", "rental-pickup", r.id, "small") : r.status === "ACTIVE" && admin ? btn("Confirm return", "rental-return", r.id, "small") : ""}</td></tr>`;
      })
      .join("") || '<tr><td colspan="5" class="muted">No rental requests yet.</td></tr>'
  }</tbody></table></div>`;
}
async function customerAccount(tab = "account") {
  let [rs, is, ps, issues, bs] = await Promise.all([
    api("/rentals"),
    api("/invoices"),
    api("/payments"),
    api("/issues"),
    api("/ride-bookings"),
  ]);
  let name = esc(me.name.split(" ")[0]);
  let content = "";
  if (tab === "account") {
    content = `<div class="dashboard-hero"><div><div class="eyebrow">YOUR NEXRIDE</div><h1>Good to see you, ${name}.</h1><p class="muted">Your bookings, payments and plans for the next ride.</p>${btn("Find a ride ↗", "goto-fleet", "", "")}</div><div class="avatar">${me.avatarUrl ? `<img src="${esc(me.avatarUrl)}" alt="" class="avatar">` : name[0]}</div></div><div class="stat-grid"><div class="stat-card"><small>Bookings</small><strong>${rs.length}</strong></div><div class="stat-card"><small>Pending</small><strong>${rs.filter((x) => x.status === "PENDING").length}</strong></div><div class="stat-card"><small>Invoices</small><strong>${is.length}</strong></div><div class="stat-card"><small>Shared rides</small><strong>${bs.length}</strong></div></div><section class="panel"><div class="panel-head"><h2>Recent rental activity</h2><a class="text-btn" href="/#rentals">View all ↗</a></div>${rentalRows(rs.slice(-5).reverse())}</section>`;
  } else if (tab === "rentals") {
    content = `<div class="eyebrow">YOUR BOOKINGS</div><h1 class="page-title">Rental requests</h1><p class="muted">Track each request from review through return.</p><div class="panel">${rentalRows(rs.slice().reverse())}</div>`;
  } else if (tab === "billing") {
    content = `<div class="eyebrow">CLEAR FROM THE START</div><h1 class="page-title">Billing & payments</h1><p class="muted">Your journey, all accounted for. Review invoices and securely pay online.</p>${paymentOverview(is)}${currencyNote()}<div class="panel"><div class="table-wrap"><table class="table"><thead><tr><th>Invoice</th><th>Rental charge</th><th>Total</th><th>Status</th><th></th></tr></thead><tbody>${is.map((i) => `<tr><td>NX-${esc(i.id.slice(0, 7))}</td><td>${esc(i.description)}</td><td>${money(i.amountLkr)}${currency === "USD" ? `<small class="field-help">${money(i.amountLkr, "LKR")} invoice</small>` : ""}</td><td>${status(i.status)}</td><td>${i.status === "UNPAID" ? btn(paymentOptions.ready ? "Pay securely" : "Payment options", "checkout", i.id, "small") : ""}</td></tr>`).join("") || '<tr><td colspan="5">No invoices yet.</td></tr>'}</tbody></table></div></div><section class="panel" style="margin-top:18px"><div class="panel-head"><h2>Payment history</h2></div><div class="table-wrap"><table class="table"><thead><tr><th>Reference</th><th>Method</th><th>Charged</th><th>Date</th></tr></thead><tbody>${ps.map((p) => `<tr><td>${esc(p.reference)}</td><td>${esc(p.method)}</td><td>${esc(p.currency)} ${esc(p.chargedAmount)}</td><td>${date(p.createdAt)}</td></tr>`).join("") || '<tr><td colspan="4">No payment records.</td></tr>'}</tbody></table></div></section>`;
  } else if (tab === "rides") {
    content = `<div class="panel-head"><div><div class="eyebrow">SHARED JOURNEYS</div><h1 class="page-title">Ride sharing</h1></div>${btn("Offer a ride +", "ride-add")}</div><div class="fleet-grid">${rides.map(rideCard).join("") || empty("No shared rides yet", "Offer a seat and start a journey.")}</div><section class="panel" style="margin-top:24px"><h2>Your passenger requests</h2>${
      bs
        .map((b) => {
          let ride = rides.find((r) => r.id === b.rideId);
          return `<div class="request-row"><span>${esc(ride ? ride.origin + " → " + ride.destination : "Shared journey")}</span>${status(b.status)}${b.status === "PENDING" && ride?.hostId === me.id ? btn("Accept", "ride-accept", b.id, "small") + btn("Decline", "ride-reject", b.id, "small outline") : ""}</div>`;
        })
        .join("") || '<p class="muted">Seat requests will appear here.</p>'
    }</section>`;
  } else if (tab === "support") {
    content = `<div class="panel-head"><div><div class="eyebrow">WE’RE HERE TO HELP</div><h1 class="page-title">Help & support</h1></div>${btn("Report an issue +", "issue-add")}</div>${issues.map((i) => `<article class="panel" style="margin:14px 0"><div class="panel-head"><h2>${esc(i.subject)}</h2>${status(i.status)}</div><p>${esc(i.description)}</p>${i.reply ? `<div class="notice">NexRide support: ${esc(i.reply)}</div>` : ""}</article>`).join("") || '<div class="empty">Your support requests will show up here.</div>'}`;
  } else if (tab === "profile") {
    content = `<div class="eyebrow">ACCOUNT DETAILS</div><h1 class="page-title">Your profile</h1><div class="panel" style="max-width:670px"><form data-form="profile">${field("name", "Full name", "text", me.name, 'required maxlength="80"')}${field("phone", "Phone number", "tel", me.phone, 'required placeholder="+94 77 123 4567"')}${field("country", "Country", "text", me.country, "required")}${select(
      "currency",
      "Preferred currency",
      [
        ["LKR", "LKR · Sri Lankan rupees"],
        ["USD", "USD · US dollars"],
      ],
      me.currency,
    )}<div class="form-group"><label>Profile photo</label><input type="file" name="photo" accept="image/png,image/jpeg"><span class="field-help">JPG or PNG · max 5 MB</span></div><div class="error-box"></div><button class="btn">Save profile</button></form><hr><form data-form="password">${field("currentPassword", "Current password", "password", "", "required")}${field("newPassword", "New password", "password", "", 'required minlength="8"')}<div class="error-box"></div><button class="btn dark">Change password</button></form></div>`;
  }
  let nav = [
    ["account", "Overview"],
    ["rentals", "Rentals"],
    ["rides", "Ride sharing"],
    ["billing", "Billing"],
    ["support", "Support"],
    ["profile", "Profile"],
  ];
  return `<main class="portal" id="main"><aside class="portal-side" id="customer-side">${brand()}<div class="portal-label">YOUR JOURNEY</div>${nav.map(([k, l]) => `<a class="portal-link ${tab === k ? "active" : ""}" href="/#${k}">${icon({ overview: "grid", account: "grid", fleet: "bike", rentals: "calendar", billing: "wallet", rides: "users", issues: "help", support: "help", users: "users", settings: "settings", profile: "user" }[k])} ${l}</a>`).join("")}<div class="portal-bottom"><div class="portal-link">${esc(me.email)}</div><button class="portal-link" data-action="logout">↗ Sign out</button></div></aside><section><header class="portal-top">${themeButton()}<div><button class="icon-btn" data-action="side-menu" aria-label="Open account menu">☰</button> <span class="muted">NexRide / ${esc(tab)}</span></div><select class="currency" data-currency><option ${currency === "LKR" ? "selected" : ""} value="LKR">LKR</option><option ${currency === "USD" ? "selected" : ""} value="USD">USD</option></select></header><div class="portal-content">${content}</div></section></main>`;
}
function adminLogin() {
  return `<main class="login-layout" id="main"><section class="login-art"><img src="/assets/sport.jpg" alt="Motorbike on an open road">${brand()}<div><div class="eyebrow">NEXRIDE OPERATIONS</div><h1>Run the fleet.<br><em>Keep moving.</em></h1><p>Bookings, bikes and customer support — one clear view.</p><small>© ${new Date().getFullYear()} NexRide. All rights reserved.</small></div></section><section class="login-form-area"><div class="login-box">${brand()}<a href="/" class="muted" style="font-size:11px">← Back to public website</a><div class="eyebrow" style="margin-top:32px">ADMIN PORTAL</div><h2>Welcome back.</h2><p>Sign in with your administrator account.</p><form data-form="admin-login">${field("identifier", "Administrator email", "email", "", 'required autocomplete="username"')}${field("password", "Password", "password", "", 'required autocomplete="current-password"')}<div class="error-box"></div><button class="btn full">Sign in to admin ↗</button></form><div class="notice">Administrator access is private. Ask the system owner if you need an account.</div><a class="text-btn" href="/" style="display:block;margin-top:18px">Customer website</a></div></section></main>`;
}
async function adminPage(tab = "overview") {
  let [summary, all, rq, invoices, issues, people, ps, bs] = await Promise.all([
    api("/admin/summary"),
    api("/bikes?all=true"),
    api("/rentals"),
    api("/invoices"),
    api("/issues"),
    api("/admin/users"),
    api("/payments"),
    api("/ride-bookings"),
  ]);
  fleet = all;
  let nav = [
    ["overview", "Overview"],
    ["fleet", "Fleet"],
    ["rentals", "Rental queue"],
    ["billing", "Billing"],
    ["rides", "Ride bookings"],
    ["issues", "Support"],
    ["users", "Customers"],
    ["settings", "Website settings"],
    ["profile", "Admin profile"],
  ];
  let content = "";
  if (tab === "overview")
    content = `<div class="eyebrow">OPERATIONS DASHBOARD</div><h1 class="page-title">Your operations, at a glance.</h1><p class="muted">A good day starts with a clear plan. Here is what needs your attention.</p><div class="stat-grid">${[
      ["Fleet vehicles", summary.vehicles],
      ["Requests to review", summary.pendingRentals],
      ["Unpaid invoices", summary.unpaidInvoices],
      ["Open issues", summary.openIssues],
    ]
      .map((x) => `<div class="stat-card"><small>${x[0]}</small><strong>${x[1]}</strong></div>`)
      .join(
        "",
      )}</div><div class="panel"><div class="panel-head"><h2>Requests awaiting your review</h2><a class="text-btn" href="/admin#rentals">View queue ↗</a></div>${rentalRows(
      rq
        .filter((r) => r.status === "PENDING")
        .slice()
        .sort((a, b) => a.createdAt.localeCompare(b.createdAt)),
      true,
    )}</div><div class="panel" style="margin-top:18px"><div class="panel-head"><h2>Quick actions</h2></div>${btn("Add a bike +", "bike-add")} ${btn("Update contact details", "goto-settings", "", "dark")}</div>`;
  if (tab === "fleet")
    content = `<div class="panel-head"><div><div class="eyebrow">MANAGE THE COLLECTION</div><h1 class="page-title">Your fleet.</h1><p class="muted">Add bikes and upload your actual fleet photographs.</p></div>${btn("Add vehicle +", "bike-add")}</div><div class="fleet-grid">${fleet.map((b) => card(b)).join("")}</div>`;
  if (tab === "rentals")
    content = `<div class="eyebrow">ARRIVAL ORDER</div><h1 class="page-title">Rental requests.</h1><p class="muted">Review oldest pending requests first.</p><div class="panel">${rentalRows(rq.slice().reverse(), true)}</div>`;
  if (tab === "billing")
    content = `<div class="eyebrow">INVOICE CONTROL</div><h1 class="page-title">Billing.</h1><div class="panel"><div class="table-wrap"><table class="table"><thead><tr><th>Invoice</th><th>Customer ID</th><th>Amount · LKR</th><th>Status</th><th>Action</th></tr></thead><tbody>${invoices.map((i) => `<tr><td>${esc(i.id.slice(0, 8))}</td><td>${esc(i.userId.slice(0, 8))}</td><td>${money(i.amountLkr, "LKR")}</td><td>${status(i.status)}</td><td>${i.status === "UNPAID" ? btn("Record cash received", "cash", i.id, "small") : ""}</td></tr>`).join("") || '<tr><td colspan="5">No invoices yet.</td></tr>'}</tbody></table></div></div><section class="panel" style="margin-top:16px"><h2>Recent payments</h2><div class="table-wrap"><table class="table"><thead><tr><th>Reference</th><th>Currency / amount</th><th>Method</th><th>Status</th></tr></thead><tbody>${ps.map((p) => `<tr><td>${esc(p.reference)}</td><td>${esc(p.currency)} ${esc(p.chargedAmount)}</td><td>${esc(p.method)}</td><td>${status(p.gatewayStatus)}</td></tr>`).join("") || '<tr><td colspan="4">No payments recorded.</td></tr>'}</tbody></table></div></section>`;
  if (tab === "rides")
    content = `<div class="eyebrow">COMMUNITY</div><h1 class="page-title">Passenger requests.</h1><div class="panel"><div class="table-wrap"><table class="table"><thead><tr><th>Booking</th><th>Ride</th><th>Customer</th><th>Status</th><th>Action</th></tr></thead><tbody>${
      bs
        .map((b) => {
          let ride = rides.find((r) => r.id === b.rideId);
          return `<tr><td>${esc(b.id.slice(0, 8))}</td><td>${esc(ride ? ride.origin + " → " + ride.destination : b.rideId)}</td><td>${esc(b.userId.slice(0, 8))}</td><td>${status(b.status)}</td><td>${b.status === "PENDING" ? btn("Accept", "ride-decision:" + b.id, "accept", "small") + `<button class="btn dark small" data-action="ride-reject" data-id="${esc(b.id)}">Reject</button>` : ""}</td></tr>`;
        })
        .join("") || '<tr><td colspan="5">No passenger requests.</td></tr>'
    }</tbody></table></div></div>`;
  if (tab === "issues")
    content = `<div class="eyebrow">CUSTOMER CARE</div><h1 class="page-title">Support inbox.</h1>${issues.map((i) => `<section class="panel" style="margin:13px 0"><div class="panel-head"><h2>${esc(i.subject)}</h2>${status(i.status)}</div><p>${esc(i.category)} · ${esc(i.userId.slice(0, 8))}</p><p class="muted">${esc(i.description)}</p>${i.status !== "RESOLVED" ? btn("Reply / resolve", "issue-resolve", i.id, "small") : ""}${i.reply ? `<div class="notice">${esc(i.reply)}</div>` : ""}</section>`).join("") || '<div class="empty">No support issues.</div>'}`;
  if (tab === "users")
    content = `<div class="eyebrow">MEMBERS</div><h1 class="page-title">Customer accounts.</h1><div class="panel"><div class="table-wrap"><table class="table"><thead><tr><th>Name</th><th>Email</th><th>Phone</th><th>Country</th><th>Status</th><th>Action</th></tr></thead><tbody>${people.map((u) => `<tr><td>${esc(u.name)}</td><td>${esc(u.email)}</td><td>${esc(u.phone || "—")}</td><td>${esc(u.country)}</td><td>${status(u.active ? "ACTIVE" : "INACTIVE")}</td><td><button class="btn ${u.active ? "dark" : ""} small" data-action="user-status" data-id="${esc(u.id)}" data-active="${u.active ? "false" : "true"}">${u.active ? "Deactivate" : "Reactivate"}</button></td></tr>`).join("") || '<tr><td colspan="6">No customer accounts.</td></tr>'}</tbody></table></div><p class="field-help">Deactivation preserves booking and payment history.</p></div>`;
  if (tab === "settings") {
    content = `<div class="eyebrow">PUBLIC WEBSITE</div><h1 class="page-title">Make it yours.</h1><p class="muted">Your changes will appear on the customer website.</p><div class="panel"><form data-form="settings" class="form-grid">${field("tagline", "Brand tagline", "text", site.tagline, 'required maxlength="100"')}<div class="form-group"><label>Website logo</label><input type="file" name="logoFile" accept="image/png,image/jpeg"><span class="field-help">PNG or JPG · max 5 MB</span></div>${field("contactEmail", "Contact email", "email", site.contactEmail, 'maxlength="160"')}${field("contactPhone", "Contact phone", "tel", site.contactPhone, 'maxlength="30"')}${field("address", "Business address", "text", site.address, 'maxlength="250"')}${field("hours", "Opening hours", "text", site.hours, 'maxlength="160"')}${field("instagramUrl", "Instagram URL", "url", site.instagramUrl, 'placeholder="https://…"')}${field("facebookUrl", "Facebook URL", "url", site.facebookUrl, 'placeholder="https://…"')}${field("lkrPerUsd", "LKR per 1 USD", "number", site.lkrPerUsd, 'min="0" max="10000" step="0.0001" required')}<div class="form-group"><label>Exchange rate source</label><div class="notice">${esc(site.rateSource)} · ${date(site.rateUpdatedAt)}<br>USD is disabled when rate is 0.</div>${btn("Refresh rate ↻", "refresh-rate", "", "dark")}</div><div class="error-box wide"></div><div class="wide"><button class="btn">Save website settings</button></div></form></div><section class="panel" style="margin-top:18px"><h2>Card payments</h2><p class="muted">Status: ${paymentOptions.ready ? "PayHere " + esc(paymentOptions.mode) + " configured" : "Not configured"}. To enable, add the real merchant environment settings and public HTTPS callback URL. Secrets are server-only.</p></section>`;
  }
  if (tab === "profile")
    content = `<div class="eyebrow">ADMIN ACCOUNT</div><h1 class="page-title">Your profile.</h1><div class="panel" style="max-width:650px"><form data-form="profile">${field("name", "Name", "text", me.name, "required")}${field("phone", "Phone number", "tel", me.phone, 'placeholder="+94…"')}${field("country", "Country", "text", me.country, "required")}${select(
      "currency",
      "Display currency",
      [
        ["LKR", "LKR"],
        ["USD", "USD"],
      ],
      me.currency,
    )}<label class="form-group">Profile photo<input type="file" name="photo" accept="image/png,image/jpeg"></label><div class="error-box"></div><button class="btn">Save profile</button></form><hr><form data-form="password">${field("currentPassword", "Current password", "password", "", "required")}${field("newPassword", "New password", "password", "", 'required minlength="8"')}<div class="error-box"></div><button class="btn dark">Change password</button></form></div>`;
  return `<main class="portal" id="main"><aside class="portal-side" id="admin-side">${brand()}<div class="portal-label">NEXRIDE OPERATIONS</div>${nav.map(([k, l]) => `<a class="portal-link ${tab === k ? "active" : ""}" href="/admin#${k}">${icon({ overview: "grid", account: "grid", fleet: "bike", rentals: "calendar", billing: "wallet", rides: "users", issues: "help", support: "help", users: "users", settings: "settings", profile: "user" }[k])} ${l}</a>`).join("")}<div class="portal-bottom"><a class="portal-link" href="/">↗ Customer website</a><button class="portal-link" data-action="logout">↗ Sign out</button></div></aside><section><header class="portal-top">${themeButton()}<div><button class="icon-btn" data-action="side-menu">☰</button> <span class="muted">Workspace / ${esc(tab)}</span></div><div class="admin-person">${me.avatarUrl ? `<img src="${esc(me.avatarUrl)}" alt="Admin profile">` : `<span class="avatar-mini">${esc(me.name[0])}</span>`}<span>${esc(me.name)}<small>Administrator</small></span></div></header><div class="portal-content">${content}</div></section></main>`;
}
function currencyNote() {
  return `<p class="muted" style="font-size:10px">${currency === "USD" ? `USD display · LKR ${esc(site.lkrPerUsd)} / USD · ${date(site.rateUpdatedAt)} · ${esc(site.rateSource)}` : "Displayed in Sri Lankan rupees (LKR)."}</p>`;
}
async function loginPage(register = false) {
  return (
    themeNav() +
    `<main class="login-layout" id="main"><section class="login-art"><img src="/assets/rider.jpg" alt="Rider preparing for a journey">${brand()}<div><div class="eyebrow">A DIFFERENT WAY TO GET THERE</div><h1>Get there.<br><em>Feel more.</em></h1><p>Join NexRide and find a new way to explore Sri Lanka.</p></div></section><section class="login-form-area"><div class="login-box">${brand()}<div class="eyebrow" style="margin-top:30px">${register ? "YOUR NEXT CHAPTER" : "YOUR NEXRIDE ACCOUNT"}</div><h2>${register ? "Create your account." : "Welcome back."}</h2><p>${register ? "Get ready to discover your next ride." : "Use your email or phone number to sign in."}</p><form data-form="${register ? "register" : "login"}">${register ? field("name", "Full name", "text", "", 'required maxlength="80"') + field("email", "Email address (Gmail supported)", "email", "", 'required autocomplete="email"') + field("phone", "Phone number", "tel", "", 'required placeholder="+94 77 123 4567"') + field("country", "Country", "text", "Sri Lanka", "required") : field("identifier", "Email or phone number", "text", "", 'required autocomplete="username" placeholder="Email address or +94…"')}${field("password", "Password", "password", "", 'required minlength="8"')}${register ? '<span class="field-help">Use an email address you can access and a password of at least 8 characters.</span>' : ""}<div class="error-box"></div><button class="btn full">${register ? "Create account" : "Sign in"} ↗</button></form><div class="divider">OR</div><p style="text-align:center">${register ? "Already a member?" : "New to NexRide?"} <a class="text-btn" href="/#${register ? "login" : "register"}">${register ? "Sign in" : "Create an account"}</a></p></div></section></main>` +
    footer()
  );
}
function modal(title, content, submit = "Continue", handler = null) {
  let m = $("#modal");
  m.className = "modal";
  m.innerHTML = `<div class="modal-title-row"><h2 id="modal-title">${title}</h2><button type="button" class="icon-btn" data-action="modal-close" aria-label="Close">×</button></div><form id="modal-form"><div>${content}</div><div class="error-box"></div><div class="modal-actions">${submit ? `<button class="btn full">${submit}</button>` : ""}</div></form>`;
  m._handler = handler;
  m.showModal();
  attachModalSubmit();
  syncVehicleFields();
}
function bikeForm(id = "") {
  let b = fleet.find((v) => v.id === id) || {
    name: "",
    kind: "MOTORCYCLE",
    location: "Colombo",
    dailyRate: 1800,
    status: "AVAILABLE",
    engineCc: 125,
    photoUrl: "",
    description: "",
  };
  modal(
    id ? "Edit vehicle" : "Add a vehicle",
    `<div class="form-grid">${field("name", "Listing name", "text", b.name, 'required maxlength="90"')}${select(
      "kind",
      "Vehicle type",
      [
        ["MOTORCYCLE", "Motorcycle"],
        ["BICYCLE", "Bicycle"],
      ],
      b.kind,
    )}${field("location", "Pickup city", "text", b.location, 'required maxlength="80"')}${field("dailyRate", "Daily rate · LKR", "number", b.dailyRate, 'required min="100" max="100000"')}${field("engineCc", "Engine capacity · cc", "number", b.engineCc || 125, 'min="50" max="2000"')}${field("gearCount", "Bicycle gears", "number", b.gearCount || 7, 'min="1" max="30"')}${select("status", "Fleet status", ["AVAILABLE", "MAINTENANCE", "RETIRED"], b.status)}<div class="form-group wide"><label>Vehicle photo</label>${b.photoUrl ? `<img class="image-preview" src="${esc(b.photoUrl)}" alt="Current vehicle photo">` : ""}<input name="photoFile" type="file" accept="image/png,image/jpeg"><span class="field-help">JPG or PNG · max 5 MB. Replace the reference image with the exact fleet photo.</span></div><div class="wide">${area("description", "Description", b.description, 3)}</div></div>`,
    id ? "Save changes" : "Add vehicle",
    async (data) => {
      let file = data.photoFile;
      delete data.photoFile;
      let url = file?.size ? await upload(file, "bike") : b.photoUrl;
      let v = {
        ...data,
        kind: data.kind,
        dailyRate: Number(data.dailyRate),
        engineCc: data.kind === "MOTORCYCLE" ? Number(data.engineCc || 125) : 0,
        gearCount: data.kind === "BICYCLE" ? Number(data.gearCount || 7) : 0,
        photoUrl: url || "",
        samplePhoto: file?.size ? false : (b.samplePhoto ?? false),
      };
      await api("/bikes" + (id ? "/" + id : ""), id ? "PUT" : "POST", v);
      await refresh();
      toast("Vehicle saved.");
    },
  );
}
async function upload(file, purpose) {
  if (!file || !file.size) return "";
  let f = new FormData();
  f.append("file", file);
  f.append("purpose", purpose);
  let x = await api("/media", "POST", f);
  return x.url;
}
function reviewDialog(id) {
  let b = fleet.find((x) => x.id === id);
  modal(
    "Tell us about the ride",
    `<div class="notice">Reviews are available after a completed rental.</div>${select(
      "rating",
      "Your rating",
      [
        [5, "★★★★★ · Excellent"],
        [4, "★★★★☆ · Good"],
        [3, "★★★☆☆ · Okay"],
        [2, "★★☆☆☆ · Could be better"],
        [1, "★☆☆☆☆ · Poor"],
      ],
      "5",
    )}${area("comment", "Your review", "", 4)}`,
    "Post review",
    async (d) => {
      await api("/reviews", "POST", {
        vehicleId: id,
        rating: Number(d.rating),
        comment: d.comment,
      });
      await refresh();
      toast("Review posted.");
    },
  );
}
async function refresh() {
  let d = await api("/public");
  fleet = d.bikes;
  rides = d.rides;
  reviews = d.reviews;
  site = d.settings;
  paymentOptions = d.paymentOptions || paymentOptions;
}
async function render() {
  adminMode = location.pathname.startsWith("/admin");
  let [route, ...args] = (
    location.hash.slice(1) ||
    (adminMode
      ? "login"
      : ["/login", "/signin"].includes(location.pathname)
        ? "login"
        : location.pathname === "/register"
          ? "register"
          : "home")
  ).split("/");
  if (!route) route = "home";
  if (!adminMode && route === "account" && new URLSearchParams(location.search).has("payment"))
    route = "billing";
  try {
    if (adminMode) {
      if (!me || me.role !== "ADMIN") {
        setApp(adminLogin());
        return;
      }
      setApp(await adminPage(["login", "register"].includes(route) ? "overview" : route));
      return;
    }
    if (route === "rides-list") {
      await refresh();
      setApp(
        headerContent(
          `<div class="eyebrow">THE COMMUNITY BOARD</div><h1 class="page-title">Find your fellow travellers.</h1><p class="muted">Offer a passenger seat or join someone heading your way.</p><p><a class="btn dark" href="/#rides">Offer a ride ↗</a></p><div class="fleet-grid">${rides.map(rideCard).join("") || empty("The road is open", "Be the first to offer a community ride.")}</div>`,
        ),
      );
      return;
    }
    if (route === "login" || route === "register") {
      setApp(await loginPage(route === "register"));
      return;
    }
    if (adminMode) {
      if (!me || me.role !== "ADMIN") {
        setApp(await adminLogin());
        return;
      }
      setApp(await adminPage(route));
      return;
    }
    if (
      route === "account" ||
      ["rentals", "billing", "rides", "support", "profile"].includes(route)
    ) {
      if (!me) {
        location.hash = "#login";
        return;
      }
      setApp(await customerAccount(route === "account" ? "account" : route));
      return;
    }
    await refresh();
    if (route === "home" || route === "how" || route === "rides" || route === "contact") {
      setApp(themeNav() + hero() + footer());
      if (route !== "home")
        requestAnimationFrame(() =>
          document.getElementById(route)?.scrollIntoView({ behavior: "smooth" }),
        );
    } else if (route === "fleet") {
      fleet = await api(
        "/bikes?" +
          new URLSearchParams({
            city: filters.city,
            kind: filters.kind,
            start: filters.start,
            end: filters.end,
          }),
      );
      setApp(fleetPage());
    } else if (route === "bike") {
      let b = fleet.find((x) => x.id === args[0]);
      setApp(bikePage(b));
      updateEstimate();
    } else {
      setApp(themeNav() + hero() + footer());
    }
  } catch (e) {
    setApp(
      themeNav() +
        `<main id="main" class="page-shell container"><h1>We hit a bump.</h1><p class="muted">${esc(e.message)}</p>${btn("Try again", "reload")}</main>` +
        footer(),
    );
  }
}
function heroContact() {
  return `<div class="panel" style="max-width:750px">${site.contactEmail ? `<p>Email: <a href="mailto:${esc(site.contactEmail)}">${esc(site.contactEmail)}</a></p>` : "<p>Contact details will be published by NexRide administration.</p>"}${site.contactPhone ? `<p>Phone: ${esc(site.contactPhone)}</p>` : ""}${site.address ? `<p>Address: ${esc(site.address)}</p>` : ""}${site.hours ? `<p>Hours: ${esc(site.hours)}</p>` : ""}${me ? `<a href="/#support" class="btn">Create a support request ↗</a>` : ""}</div>`;
}
function attachModalSubmit() {
  let form = $("#modal-form");
  if (form)
    form.onsubmit = async (e) => {
      e.preventDefault();
      let button = $('button[type="submit"]', form) || $("button.btn", form),
        old = button?.textContent;
      if (button) {
        button.disabled = true;
        button.textContent = "Please wait…";
      }
      try {
        let d = Object.fromEntries(new FormData(form));
        let result = await form.closest("dialog")._handler?.(d);
        if (result === false) return;
        form.closest("dialog").close();
        if (location.pathname.startsWith("/admin")) await render();
        else if (
          location.hash === "#account" ||
          ["#billing", "#rentals", "#rides", "#support", "#profile"].includes(location.hash)
        )
          await render();
      } catch (e) {
        err(e.message);
      } finally {
        if (button) {
          button.disabled = false;
          button.textContent = old;
        }
      }
    };
}
function setRoute(route) {
  location.hash = "#" + route;
}

document.addEventListener("submit", async (e) => {
  let form = e.target;
  if (!(form instanceof HTMLFormElement)) return;
  let kind = form.dataset.form;
  if (kind) {
    e.preventDefault();
    let button = $('button[type="submit"]', form) || $("button.btn", form);
    if (button) {
      button.disabled = true;
      button.dataset.old = button.textContent;
      button.textContent = "Please wait…";
    }
    let d = Object.fromEntries(new FormData(form));
    let error = $(".error-box", form);
    if (error) error.textContent = "";
    try {
      if (kind === "login" || kind === "admin-login") {
        let res = await api(kind === "login" ? "/login" : "/admin/login", "POST", {
          identifier: d.identifier,
          password: d.password,
        });
        me = res.user;
        csrf = res.csrf;
        if (kind === "admin-login") location.assign("/admin#overview");
        else location.assign("/#account");
        return;
      }
      if (kind === "register") {
        let res = await api("/register", "POST", d);
        let login = await api("/login", "POST", { identifier: d.email, password: d.password });
        me = login.user;
        csrf = login.csrf;
        location.assign("/#account");
        return;
      }
      if (kind === "profile") {
        let photo = await upload(d.photo, "profile");
        let payload = {
          name: d.name,
          phone: d.phone,
          country: d.country,
          currency: d.currency,
          avatarUrl: photo || me.avatarUrl || "",
        };
        me = await api("/profile", "PUT", payload);
        localStorage.setItem("nexride-currency", me.currency);
        currency = me.currency;
        toast("Profile updated.");
        await render();
        return;
      }
      if (kind === "settings") {
        let logo = await upload(d.logoFile, "logo");
        delete d.logoFile;
        let saved = {
          ...site,
          ...d,
          logoUrl: logo || site.logoUrl,
          lkrPerUsd: Number(d.lkrPerUsd),
        };
        site = await api("/settings", "PUT", saved);
        toast("Website details saved.");
        await render();
        return;
      }
      if (kind === "password") {
        await api("/profile/password", "PUT", d);
        toast("Password changed.");
        form.reset();
        return;
      }
    } catch (ex) {
      if (error) error.textContent = ex.message;
      else toast(ex.message);
    } finally {
      if (button) {
        button.disabled = false;
        button.textContent = button.dataset.old || "Save";
      }
    }
  }
  if (form.id === "search-form") {
    e.preventDefault();
    let d = Object.fromEntries(new FormData(form));
    if (Boolean(d.start) !== Boolean(d.end)) {
      $(".search-error", form).textContent = "Choose both pick-up and return dates.";
      return;
    }
    if (d.start && d.end && d.end < d.start) {
      $(".search-error", form).textContent = "Return date must be on or after pick-up.";
      return;
    }
    filters = { ...filters, ...d };
    fleet = await api(
      "/bikes?" +
        new URLSearchParams({
          city: filters.city,
          kind: filters.kind,
          start: filters.start,
          end: filters.end,
        }),
    );
    if (location.hash === "#fleet") await render();
    else setRoute("fleet");
    return;
  }
  if (form.id === "modal-form") {
    e.preventDefault();
  }
});
$("#modal").addEventListener("close", () => {
  $("#modal")._handler = null;
});
document.addEventListener("click", async (e) => {
  let currencyTarget = e.target.closest("[data-currency]");
  if (currencyTarget) return;
  let kind = e.target.closest("[data-kind]");
  if (kind) {
    filters.kind = kind.dataset.kind;
    fleet = await api(
      "/bikes?" +
        new URLSearchParams({
          kind: filters.kind,
          city: filters.city,
          start: filters.start,
          end: filters.end,
        }),
    );
    if (location.hash.startsWith("#fleet")) await render();
    else if (location.hash === "#fleet") await render();
    else setRoute("fleet");
    return;
  }
  let actionNode = e.target.closest("[data-action]");
  if (!actionNode) return;
  let action = actionNode.dataset.action,
    id = actionNode.dataset.id;
  try {
    switch (action) {
      case "theme": {
        let next = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
        document.documentElement.dataset.theme = next;
        try {
          localStorage.setItem("nexride-theme", next);
        } catch (e) {}
        syncThemeButtons();
        break;
      }
      case "menu":
        $("#navlinks")?.classList.toggle("open");
        actionNode.setAttribute(
          "aria-expanded",
          String($("#navlinks")?.classList.contains("open")),
        );
        break;
      case "side-menu":
        $(".portal-side")?.classList.toggle("open");
        break;
      case "modal-close":
        $("#modal").close();
        break;
      case "reload":
        await render();
        break;
      case "goto-fleet":
        location.assign("/#fleet");
        break;
      case "goto-settings":
        location.assign("/admin#settings");
        break;
      case "logout":
        await api("/logout", "POST", {});
        me = null;
        csrf = "";
        location.assign(adminMode ? "/admin#login" : "/");
        break;
      case "bike":
        location.hash = "#bike/" + id;
        break;
      case "bike-add":
        bikeForm();
        break;
      case "bike-edit":
        bikeForm(id);
        break;
      case "bike-delete":
        if (confirm("Delete this vehicle? Active bookings prevent deletion.")) {
          await api("/bikes/" + id, "DELETE");
          await render();
          toast("Vehicle deleted.");
        }
        break;
      case "request-rental":
        if (!me) {
          location.hash = "#login";
          break;
        }
        modal(
          "Request this ride",
          `<div class="form-group"><label for="f-startDate">Pick-up</label><input id="f-startDate" name="startDate" type="date" min="${today()}" value="${filters.start || today()}" required></div>${field("endDate", "Return date", "date", filters.end || filters.start || today(), `min="${today()}" required`)}${area("note", "A note for the team", "", 2)}<div class="notice">The team confirms availability before an invoice is created. 7+ days receive 10% off.</div>`,
          "Send request",
          async (d) => {
            await api("/rentals", "POST", {
              vehicleId: id,
              startDate: d.startDate,
              endDate: d.endDate,
              note: d.note,
            });
            toast("Rental request sent.");
            location.hash = "#rentals";
          },
        );
        break;
      case "rental-approve":
        if (confirm("Approve this rental and create the invoice?")) {
          await api("/rentals/" + id + "/action", "POST", { action: "approve" });
          await render();
          toast("Rental approved and invoice created.");
        }
        break;
      case "rental-reject": {
        let note = prompt("Reason for declining this request:");
        if (note === null) break;
        await api("/rentals/" + id + "/action", "POST", { action: "reject", note });
        await render();
        break;
      }
      case "rental-cancel":
        if (confirm("Cancel this pending request?")) {
          await api("/rentals/" + id + "/action", "POST", { action: "cancel" });
          await render();
          toast("Request cancelled.");
        }
        break;
      case "rental-pickup":
        await api("/rentals/" + id + "/action", "POST", { action: "pickup" });
        await render();
        toast("Pickup recorded.");
        break;
      case "rental-return":
        await api("/rentals/" + id + "/action", "POST", { action: "return" });
        await render();
        toast("Return recorded.");
        break;
      case "ride-add":
        if (!me) {
          location.hash = "#login";
          break;
        }
        modal(
          "Offer a community ride",
          `<div class="notice">Community ride sharing · no ride fare is charged.</div>${field("origin", "From", "text", "", "required")}${field("destination", "To", "text", "", "required")}${field("departure", "Departure · local time", "datetime-local", "", "required")}${field("seats", "Passenger seats", "number", "1", 'min="1" max="4" required')}${area("description", "Ride details", "", 3)}`,
          "Publish ride",
          async (d) => {
            await api("/rides", "POST", { ...d, seats: Number(d.seats) });
            await refresh();
            toast("Ride offer published.");
            location.hash = "#rides";
          },
        );
        break;
      case "ride-join":
        await api("/ride-bookings", "POST", { rideId: id });
        toast("Seat request sent to the host.");
        break;
      case "ride-accept":
        await api("/ride-bookings/" + id + "/decision", "POST", { decision: "accept" });
        await render();
        toast("Passenger request accepted.");
        break;
      case "ride-reject":
        await api("/ride-bookings/" + id + "/decision", "POST", { decision: "reject" });
        await render();
        toast("Passenger request declined.");
        break;
      case "checkout":
        await openCheckout(id);
        break;
      case "user-status": {
        let enabled = actionNode.dataset.active === "true";
        if (confirm((enabled ? "Reactivate" : "Deactivate") + " this customer account?")) {
          await api("/admin/users/" + id + "/status", "PUT", { active: enabled });
          await render();
          toast(enabled ? "Customer account reactivated." : "Customer account deactivated.");
        }
        break;
      }
      case "cash":
        if (confirm("Confirm that you have actually received this cash payment?")) {
          await api("/invoices/" + id + "/cash", "POST", {});
          await render();
          toast("Cash receipt recorded.");
        }
        break;
      case "issue-add":
        modal(
          "Report an issue",
          `${select("category", "Topic", ["Bike or bicycle", "Rental request", "Payment", "Ride sharing", "Account", "Other"])}${field("subject", "Subject", "text", "", 'required maxlength="120"')}${area("description", "What happened?", "", 5)}`,
          "Send to support",
          async (d) => {
            await api("/issues", "POST", d);
            toast("Your issue was sent to support.");
            await render();
          },
        );
        break;
      case "issue-resolve": {
        let note = prompt("Write a reply to the customer:");
        if (note === null) break;
        await api("/issues/" + id + "/decision", "PUT", { reply: note, status: "RESOLVED" });
        await render();
        toast("Issue reply saved.");
        break;
      }
      case "review":
        reviewDialog(id);
        break;
      case "refresh-rate":
        await api("/settings/exchange-rate", "POST", {});
        await refresh();
        toast("Exchange rate refreshed.");
        await render();
        break;
      default:
        if (action.startsWith("ride-decision:")) {
          await api("/ride-bookings/" + action.split(":")[1] + "/decision", "POST", {
            decision: id,
          });
          await render();
        }
    }
  } catch (ex) {
    toast(ex.message);
  }
});
document.addEventListener("change", (e) => {
  if (e.target.matches("[data-currency]")) {
    currency = e.target.value;
    localStorage.setItem("nexride-currency", currency);
    render();
  }
  if (e.target.name === "kind" && e.target.closest("#modal-form")) syncVehicleFields();
});
window.addEventListener("hashchange", () => {
  if ($("#modal").open) $("#modal").close();
  render();
  window.scrollTo({ top: 0, behavior: "smooth" });
});
function observeReveal() {
  if (!("IntersectionObserver" in window)) {
    $$(".reveal").forEach((x) => x.classList.add("in"));
    return;
  }
  let io = new IntersectionObserver(
    (entries) =>
      entries.forEach((x) => {
        if (x.isIntersecting) {
          x.target.classList.add("in");
          io.unobserve(x.target);
        }
      }),
    { threshold: 0.1 },
  );
  $$(".reveal").forEach((x) => io.observe(x));
}
window.addEventListener(
  "scroll",
  () => $(".topbar")?.classList.toggle("scrolled", window.scrollY > 40),
  { passive: true },
);
document.addEventListener("pointermove", (e) => {
  let c = $(".cursor-glow");
  if (c) {
    c.style.left = e.clientX + "px";
    c.style.top = e.clientY + "px";
  }
});
(async () => {
  try {
    let s = await api("/session");
    me = s.user?.id ? s.user : null;
    csrf = s.csrf;
    await refresh();
    await render();
  } catch (e) {
    setApp(
      `<main class="page-shell container"><h1>NexRide is starting…</h1><p class="muted">${esc(e.message)}</p><a class="btn" href="/">Reload</a></main>`,
    );
  }
})();

document.addEventListener("change", (e) => {
  if (e.target.id === "fleet-sort") {
    filters.sort = e.target.value;
    render();
  }
  if (["f-detailStart", "f-detailEnd"].includes(e.target.id)) updateEstimate();
  if (e.target.id === "q-start") $("#q-end").min = e.target.value || today();
  if (e.target.type === "file" && e.target.files[0]) {
    let file = e.target.files[0];
    if (file.size > 5 * 1024 * 1024) {
      toast("Choose an image smaller than 5 MB.");
      e.target.value = "";
      return;
    }
    let prev = e.target.parentElement.querySelector(".image-preview");
    if (!prev) {
      prev = document.createElement("img");
      prev.className = "image-preview";
      prev.alt = "Selected image preview";
      e.target.before(prev);
    }
    let reader = new FileReader();
    reader.onload = () => (prev.src = reader.result);
    reader.readAsDataURL(file);
  }
});

function syncVehicleFields() {
  let f = $("#modal-form");
  if (!f?.elements.kind) return;
  for (let [name, kind] of [
    ["engineCc", "MOTORCYCLE"],
    ["gearCount", "BICYCLE"],
  ]) {
    let el = f.elements[name];
    if (el) {
      let enabled = f.elements.kind.value === kind;
      el.disabled = !enabled;
      el.parentElement.hidden = !enabled;
      el.parentElement.style.display = enabled ? "flex" : "none";
    }
  }
}

function themeButton() {
  return '<button type="button" class="theme-toggle icon-btn" data-action="theme" aria-label="Switch colour theme">◐</button>';
}
function syncThemeButtons() {
  let dark = document.documentElement.dataset.theme === "dark";
  document.querySelectorAll('[data-action="theme"]').forEach((b) => {
    b.textContent = dark ? "☀" : "☾";
    b.setAttribute("aria-label", dark ? "Switch to light theme" : "Switch to dark theme");
    b.setAttribute("aria-pressed", String(dark));
  });
}
function paymentOverview(invoices) {
  let unpaid = invoices.filter((i) => i.status === "UNPAID");
  let result = new URLSearchParams(location.search).get("payment");
  return `<div class="payment-overview"><div><span class="eyebrow">OUTSTANDING BALANCE</span><strong>${money(unpaid.reduce((n, i) => n + i.amountLkr, 0))}</strong><p>${unpaid.length} unpaid invoice${unpaid.length === 1 ? "" : "s"}</p></div><div><div class="card-brands"><span>VISA</span><span>Mastercard</span></div><h3>${paymentOptions.ready ? (paymentOptions.mode === "sandbox" ? "Test checkout enabled" : "Secure online checkout") : "Card payments coming soon"}</h3><p>${paymentOptions.ready ? "Choose LKR or USD. Review your exact charge before continuing to PayHere." : "Contact the team to arrange payment. Your invoice remains unpaid until a payment is confirmed."}</p></div></div>${result ? `<div class="notice" role="status">${result === "cancel" ? "Checkout was cancelled. No payment confirmation was received from this return." : "You have returned from checkout. Only a verified payment notification can mark your invoice paid."} Check the invoice status below. ${btn("Refresh status", "reload", "", "small outline")}</div>` : ""}`;
}
async function openCheckout(id) {
  let invoices = await api("/invoices");
  let i = invoices.find((x) => x.id === id);
  if (!i || i.status !== "UNPAID")
    throw Error("This invoice is no longer available for payment. Refresh your billing page.");
  if (!paymentOptions.ready) {
    modal(
      "Payment options",
      `<div class="card-brands"><span>VISA</span><span>Mastercard</span></div><h3>${money(i.amountLkr)}</h3><p>Online card checkout is not enabled yet. Contact NexRide to arrange payment for invoice NX-${esc(i.id.slice(0, 7))}.</p><a class="btn full" href="/#support" data-action="modal-close">Contact support</a>`,
      null,
    );
    return;
  }
  modal(
    "Your secure checkout",
    `<div class="checkout-invoice"><span>Invoice NX-${esc(i.id.slice(0, 7))}</span><strong>${money(i.amountLkr, "LKR")}</strong><p>${esc(i.description)}</p></div><div class="card-brands"><span>VISA</span><span>Mastercard</span></div><div class="notice">${paymentOptions.mode === "sandbox" ? "TEST MODE · No real card charge." : "Card details are entered only on PayHere’s secure payment page."}</div>${select(
      "currency",
      "Payment currency",
      Number(site.lkrPerUsd) > 0
        ? [
            ["LKR", "LKR · Sri Lankan rupees"],
            ["USD", "USD · US dollars"],
          ]
        : [["LKR", "LKR · Sri Lankan rupees"]],
      currency,
    )}${field("address", "Billing address", "text", "", 'required autocomplete="street-address"')}${field("city", "City", "text", "", 'required autocomplete="address-level2"')}${field("country", "Country", "text", me.country || "Sri Lanka", 'required autocomplete="country-name"')}<p class="field-help">Next, review the exact amount supplied by our server. Your bank may apply currency conversion fees.</p>`,
    "Review payment",
    async (d) => {
      let c = await api("/invoices/" + id + "/checkout", "POST", d);
      if (
        ![
          "https://sandbox.payhere.lk/pay/checkout",
          "https://www.payhere.lk/pay/checkout",
        ].includes(c.action)
      )
        throw Error("Unexpected payment provider.");
      let form = $("#modal-form");
      form.innerHTML = `<div class="checkout-review"><span class="eyebrow">TOTAL TO PAY</span><h2>${esc(c.fields.currency)} ${esc(c.fields.amount)}</h2><p>Invoice NX-${esc(i.id.slice(0, 7))}</p><div class="notice">${c.mode === "sandbox" ? "Sandbox payment — test transactions only." : "You will now leave NexRide for secure PayHere checkout."}</div><p>Your invoice becomes paid only after verified confirmation from the provider.</p><div class="error-box"></div><button class="btn full" type="submit">Continue to PayHere ↗</button></div>`;
      $("#modal")._handler = async () => {
        let f = document.createElement("form");
        f.method = "post";
        f.action = c.action;
        for (let [k, v] of Object.entries(c.fields)) {
          let input = document.createElement("input");
          input.type = "hidden";
          input.name = k;
          input.value = v;
          f.append(input);
        }
        document.body.append(f);
        f.submit();
        return false;
      };
      attachModalSubmit();
      return false;
    },
  );
}