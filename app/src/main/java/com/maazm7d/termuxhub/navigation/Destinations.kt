package com.maazm7d.termuxhub.navigation

/**
 * Centralized navigation routes for TermuxHub
 *
 * Rules:
 * - Bottom navigation routes = top-level destinations
 * - Detail / sub screens are NOT part of bottom navigation
 */
object Destinations {

    // Splash / entry
    const val SPLASH = "splash"

    // ─────────────────────────────────────────────
    // Bottom navigation (TOP-LEVEL destinations)
    // ─────────────────────────────────────────────
    const val TOOLS = "tools"
    const val SAVED = "saved"
    const val HALL = "hall"
    const val WHATS_NEW = "whats_new"
    const val ABOUT = "about"

    // ─────────────────────────────────────────────
    // Sub-screens (NOT in bottom navigation)
    // ─────────────────────────────────────────────
    const val DETAILS = "details"
}
