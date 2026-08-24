type IconProps = {
  className?: string
}

export function LogoMark({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 48 48" aria-hidden="true">
      <rect x="4" y="4" width="18" height="18" rx="6" fill="var(--accent)" />
      <rect x="26" y="4" width="18" height="18" rx="6" fill="none" stroke="var(--accent)" strokeWidth="3" />
      <rect x="4" y="26" width="18" height="18" rx="6" fill="none" stroke="var(--accent)" strokeWidth="3" />
      <rect x="26" y="26" width="18" height="18" rx="6" fill="var(--accent)" />
    </svg>
  )
}

export function GearIcon({ className }: IconProps) {
  return (
    <svg
      className={className}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="1.6"
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
    >
      <path d="M10.3 2.5h3.4l.6 2.7a7 7 0 011.9 1.1l2.6-.9 1.7 3-2.1 1.8a7 7 0 010 2.2l2.1 1.8-1.7 3-2.6-.9a7 7 0 01-1.9 1.1l-.6 2.7h-3.4l-.6-2.7a7 7 0 01-1.9-1.1l-2.6.9-1.7-3 2.1-1.8a7 7 0 010-2.2L3.5 8.4l1.7-3 2.6.9a7 7 0 011.9-1.1z" />
      <circle cx="12" cy="12" r="3" />
    </svg>
  )
}

export function ChevronRightIcon({ className }: IconProps) {
  return (
    <svg
      className={className}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
    >
      <path d="M9 6l6 6-6 6" />
    </svg>
  )
}

export function BarChartIcon({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6" aria-hidden="true">
      <path d="M4 19V5M10 19V9M16 19V12M22 19V6" />
    </svg>
  )
}

export function FlameIcon({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6" aria-hidden="true">
      <path d="M12 2c1 3-3 4-3 8a3 3 0 006 0c0-1-1-2-1-3 2 1 3 3 3 5a5 5 0 01-10 0c0-4 3-6 5-10z" />
    </svg>
  )
}

export function StarIcon({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6" aria-hidden="true">
      <path d="M12 2l2.9 6.3 6.9.7-5.2 4.7 1.6 6.8L12 17l-6.2 3.5 1.6-6.8L2.2 9l6.9-.7z" />
    </svg>
  )
}

export function StarFilledIcon({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" width="18" height="18" fill="currentColor" aria-hidden="true">
      <path d="M12 2.5l2.9 6 6.6.6-5 4.4 1.5 6.5L12 16.8 6 20l1.5-6.5-5-4.4 6.6-.6z" />
    </svg>
  )
}

export function TrophyIcon({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6" aria-hidden="true">
      <path d="M8 21h8M12 17v4M7 4h10l-1 8.5a4 4 0 01-8 0z" />
    </svg>
  )
}

export function GoogleIcon({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 48 48" aria-hidden="true">
      <path
        fill="#FFC107"
        d="M43.6 20.5H42V20H24v8h11.3C33.9 32.9 29.4 36 24 36c-6.6 0-12-5.4-12-12s5.4-12 12-12c3.1 0 5.9 1.2 8 3.1l5.7-5.7C34.5 6.1 29.5 4 24 4 12.9 4 4 12.9 4 24s8.9 20 20 20 20-8.9 20-20c0-1.3-.1-2.7-.4-3.5z"
      />
      <path
        fill="#FF3D00"
        d="M6.3 14.7l6.6 4.8C14.6 15.9 18.9 13 24 13c3.1 0 5.9 1.2 8 3.1l5.7-5.7C34.5 6.1 29.5 4 24 4c-7.7 0-14.4 4.4-17.7 10.7z"
      />
      <path
        fill="#4CAF50"
        d="M24 44c5.3 0 10.1-2 13.7-5.4l-6.3-5.3C29.4 35 26.9 36 24 36c-5.3 0-9.8-3.1-11.3-7.6l-6.5 5C9.5 39.6 16.2 44 24 44z"
      />
      <path
        fill="#1976D2"
        d="M43.6 20.5H42V20H24v8h11.3c-.7 2.1-2.1 3.9-3.9 5.2l6.3 5.3C41.4 35.8 44 30.4 44 24c0-1.3-.1-2.7-.4-3.5z"
      />
    </svg>
  )
}
