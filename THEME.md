# 🎨 FPMS UI Theme Guidelines
### Intelligent Final Year Project Management System

---

## Theme Overview

| Property | Value |
|---|---|
| **Style** | Academic & Formal |
| **Mode** | Light |
| **Primary Palette** | Navy + Gold |
| **Framework** | Plain CSS |
| **Font Stack** | Inter / Georgia |

---

## 🎨 Color Palette

### Primary Colors

| Name | Hex | Usage |
|---|---|---|
| **Navy Deep** | `#0A1628` | Sidebar, navbar background |
| **Navy Primary** | `#1B3A6B` | Primary buttons, headings |
| **Navy Mid** | `#2E5399` | Hover states, active links |
| **Navy Light** | `#D6E4F7` | Table headers, card backgrounds |

### Accent Colors (Gold)

| Name | Hex | Usage |
|---|---|---|
| **Gold Primary** | `#C9A84C` | Highlights, badges, icons |
| **Gold Light** | `#F0D080` | Hover on gold elements |
| **Gold Muted** | `#E8D5A3` | Subtle backgrounds, dividers |

### Neutral Colors

| Name | Hex | Usage |
|---|---|---|
| **White** | `#FFFFFF` | Page background, cards |
| **Off White** | `#F7F8FC` | Section backgrounds |
| **Grey Light** | `#E5E9F0` | Borders, dividers |
| **Grey Mid** | `#9AA3B2` | Placeholder text, icons |
| **Grey Dark** | `#4A5568` | Body text, labels |
| **Black** | `#1A202C` | Headings, strong text |

### Semantic Colors

| Name | Hex | Usage |
|---|---|---|
| **Success** | `#2E7D52` | Approved status, success alerts |
| **Warning** | `#B45309` | Similarity warnings, pending |
| **Danger** | `#C0392B` | Rejected status, high similarity flag |
| **Info** | `#2E5399` | Info alerts, tooltips |

---

## 🖋️ Typography

### Font Stack
```css
/* Headings */
font-family: 'Georgia', 'Times New Roman', serif;

/* Body & UI */
font-family: 'Inter', 'Segoe UI', Arial, sans-serif;
```

### Type Scale

| Element | Size | Weight | Color |
|---|---|---|---|
| Page Title (h1) | 2rem | 700 | `#1B3A6B` |
| Section Title (h2) | 1.5rem | 600 | `#1B3A6B` |
| Card Title (h3) | 1.2rem | 600 | `#1A202C` |
| Body Text | 1rem | 400 | `#4A5568` |
| Small / Label | 0.875rem | 500 | `#9AA3B2` |
| Table Header | 0.875rem | 600 | `#1B3A6B` |

---

## 🧩 Component Styles

### Navbar
```css
.navbar {
    background-color: #0A1628;
    color: #FFFFFF;
    padding: 0 2rem;
    height: 64px;
    border-bottom: 3px solid #C9A84C;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.navbar-brand {
    font-family: 'Georgia', serif;
    font-size: 1.25rem;
    font-weight: 700;
    color: #C9A84C;
    letter-spacing: 0.5px;
}

.navbar a {
    color: #D6E4F7;
    text-decoration: none;
    font-size: 0.9rem;
    padding: 0 1rem;
    transition: color 0.2s;
}

.navbar a:hover {
    color: #C9A84C;
}
```

### Sidebar
```css
.sidebar {
    background-color: #1B3A6B;
    width: 250px;
    min-height: 100vh;
    padding: 1.5rem 0;
    border-right: 1px solid #2E5399;
}

.sidebar-item {
    padding: 0.75rem 1.5rem;
    color: #D6E4F7;
    font-size: 0.9rem;
    cursor: pointer;
    transition: background 0.2s;
    border-left: 3px solid transparent;
}

.sidebar-item:hover,
.sidebar-item.active {
    background-color: #2E5399;
    border-left: 3px solid #C9A84C;
    color: #FFFFFF;
}
```

### Buttons
```css
/* Primary Button */
.btn-primary {
    background-color: #1B3A6B;
    color: #FFFFFF;
    border: none;
    padding: 0.6rem 1.4rem;
    border-radius: 4px;
    font-size: 0.9rem;
    font-weight: 500;
    cursor: pointer;
    transition: background 0.2s;
}

.btn-primary:hover {
    background-color: #2E5399;
}

/* Accent Button */
.btn-accent {
    background-color: #C9A84C;
    color: #0A1628;
    border: none;
    padding: 0.6rem 1.4rem;
    border-radius: 4px;
    font-size: 0.9rem;
    font-weight: 600;
    cursor: pointer;
    transition: background 0.2s;
}

.btn-accent:hover {
    background-color: #F0D080;
}

/* Outline Button */
.btn-outline {
    background-color: transparent;
    color: #1B3A6B;
    border: 1.5px solid #1B3A6B;
    padding: 0.6rem 1.4rem;
    border-radius: 4px;
    font-size: 0.9rem;
    cursor: pointer;
    transition: all 0.2s;
}

.btn-outline:hover {
    background-color: #1B3A6B;
    color: #FFFFFF;
}

/* Danger Button */
.btn-danger {
    background-color: #C0392B;
    color: #FFFFFF;
    border: none;
    padding: 0.6rem 1.4rem;
    border-radius: 4px;
    font-size: 0.9rem;
    cursor: pointer;
}
```

### Cards
```css
.card {
    background-color: #FFFFFF;
    border: 1px solid #E5E9F0;
    border-radius: 6px;
    padding: 1.5rem;
    box-shadow: 0 1px 4px rgba(10, 22, 40, 0.08);
}

.card-header {
    font-family: 'Georgia', serif;
    font-size: 1.1rem;
    font-weight: 600;
    color: #1B3A6B;
    padding-bottom: 0.75rem;
    margin-bottom: 1rem;
    border-bottom: 2px solid #C9A84C;
}
```

### Forms & Inputs
```css
.form-label {
    font-size: 0.875rem;
    font-weight: 500;
    color: #4A5568;
    margin-bottom: 0.35rem;
    display: block;
}

.form-input,
.form-textarea,
.form-select {
    width: 100%;
    padding: 0.6rem 0.9rem;
    border: 1.5px solid #E5E9F0;
    border-radius: 4px;
    font-size: 0.9rem;
    color: #1A202C;
    background-color: #FFFFFF;
    transition: border-color 0.2s;
    box-sizing: border-box;
}

.form-input:focus,
.form-textarea:focus,
.form-select:focus {
    outline: none;
    border-color: #1B3A6B;
    box-shadow: 0 0 0 3px rgba(27, 58, 107, 0.1);
}

.form-textarea {
    min-height: 120px;
    resize: vertical;
}
```

### Tables
```css
.table {
    width: 100%;
    border-collapse: collapse;
    font-size: 0.9rem;
}

.table thead tr {
    background-color: #1B3A6B;
    color: #FFFFFF;
}

.table thead th {
    padding: 0.85rem 1rem;
    text-align: left;
    font-weight: 600;
    font-size: 0.85rem;
    letter-spacing: 0.3px;
}

.table tbody tr {
    border-bottom: 1px solid #E5E9F0;
    transition: background 0.15s;
}

.table tbody tr:hover {
    background-color: #D6E4F7;
}

.table tbody td {
    padding: 0.75rem 1rem;
    color: #4A5568;
}
```

### Status Badges
```css
.badge {
    display: inline-block;
    padding: 0.25rem 0.75rem;
    border-radius: 20px;
    font-size: 0.78rem;
    font-weight: 600;
    letter-spacing: 0.3px;
}

.badge-submitted  { background-color: #D6E4F7; color: #1B3A6B; }
.badge-approved   { background-color: #D4EDDA; color: #2E7D52; }
.badge-rejected   { background-color: #FDECEA; color: #C0392B; }
.badge-draft      { background-color: #E5E9F0; color: #4A5568; }
.badge-warning    { background-color: #FEF3CD; color: #B45309; }
```

### Similarity Score Indicator
```css
.similarity-score {
    font-weight: 700;
    font-size: 1rem;
}

.similarity-high   { color: #C0392B; } /* > 0.82 — flag */
.similarity-medium { color: #B45309; } /* 0.65 – 0.82 — warn */
.similarity-low    { color: #2E7D52; } /* < 0.65 — clear */

.similarity-bar {
    height: 8px;
    border-radius: 4px;
    background-color: #E5E9F0;
    overflow: hidden;
}

.similarity-bar-fill {
    height: 100%;
    border-radius: 4px;
    transition: width 0.4s ease;
}

.similarity-bar-fill.high   { background-color: #C0392B; }
.similarity-bar-fill.medium { background-color: #B45309; }
.similarity-bar-fill.low    { background-color: #2E7D52; }
```

### Alerts
```css
.alert {
    padding: 0.9rem 1.2rem;
    border-radius: 4px;
    font-size: 0.9rem;
    margin-bottom: 1rem;
    border-left: 4px solid;
}

.alert-success { background: #D4EDDA; color: #2E7D52; border-color: #2E7D52; }
.alert-warning { background: #FEF3CD; color: #B45309; border-color: #B45309; }
.alert-danger  { background: #FDECEA; color: #C0392B; border-color: #C0392B; }
.alert-info    { background: #D6E4F7; color: #1B3A6B; border-color: #1B3A6B; }
```

---

## 📐 Layout & Spacing

```css
/* Page wrapper */
.page-wrapper {
    display: flex;
    min-height: 100vh;
    background-color: #F7F8FC;
}

/* Main content area */
.main-content {
    flex: 1;
    padding: 2rem;
    max-width: 1200px;
}

/* Section spacing */
.section { margin-bottom: 2rem; }
.section-title {
    font-family: 'Georgia', serif;
    font-size: 1.4rem;
    color: #1B3A6B;
    margin-bottom: 1.25rem;
    padding-bottom: 0.5rem;
    border-bottom: 2px solid #C9A84C;
}
```

### Spacing Scale

| Token | Value | Usage |
|---|---|---|
| xs | 4px | Icon gaps, tight spacing |
| sm | 8px | Input padding, small gaps |
| md | 16px | Component padding |
| lg | 24px | Section spacing |
| xl | 32px | Page sections |
| 2xl | 48px | Major layout gaps |

---

## 🖼️ Page-Specific Guidelines

### Login / Landing Page
- Full-page navy (`#0A1628`) background
- Centered white card with gold accent border on top
- University logo centered above the form
- Gold submit button

### Student Dashboard
- White sidebar with navy active state
- Gold accent on active menu item
- Cards for proposal status, chapter progress
- Similarity score shown prominently with color-coded bar

### Supervisor Dashboard
- Same layout as student
- Proposals listed in table with similarity score badge
- Approve (navy) / Reject (danger) action buttons per row

### Proposal Submission Form
- Clean white card layout
- Each field clearly labelled
- Character count on text areas
- Submit button in gold accent

---

## ✅ Quick Reference

```
Background:     #F7F8FC
Surface/Card:   #FFFFFF
Primary:        #1B3A6B  (Navy)
Accent:         #C9A84C  (Gold)
Text Primary:   #1A202C
Text Secondary: #4A5568
Border:         #E5E9F0
Success:        #2E7D52
Warning:        #B45309
Danger:         #C0392B
```

---

*FPMS UI Theme — Academic & Formal, Navy + Gold, Light Mode*
