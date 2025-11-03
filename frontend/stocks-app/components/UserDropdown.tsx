"use client"

import React from "react"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {useRouter} from "next/navigation"
import {Avatar, AvatarFallback} from "@/components/ui/avatar"
import {Button} from "./ui/button"
import {LogOut} from "lucide-react"
import NavItems from "./NavItems"

export const UserDropdown = ({
  user,
}: {
  user: {id: string; name: string; email: string} | undefined
}) => {
  const router = useRouter()

  // Debug: log user to see what's being passed
  console.log("UserDropdown user:", user)

  const handleSignOut = async () => {
    try {
      const res = await fetch("/api/auth/sign-out", {method: "POST"})

      if (!res.ok) {
        console.error("Sign out failed:", await res.text())
      }

      // Clear client-side user state
      try {
        localStorage.removeItem("user")
      } catch {
        // ignore storage errors in SSR or restricted environments
      }

      router.push("/sign-in")
    } catch (err) {
      console.error("Sign out error:", err)
      router.push("/sign-in")
    }
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant="ghost"
          className="flex items-center gap-3 text-gray-4 hover:text-yellow-500"
        >
          <Avatar className="h-8 w-8 ">
            {/* Remove or comment out AvatarImage to see the fallback */}
            {/* <AvatarImage src="https://github.com/shadcn.png" /> */}
            <AvatarFallback className="bg-yellow-500 text-yellow-900 text-sm font-bold">
              {user?.name?.[0]?.toUpperCase() || "U"}
            </AvatarFallback>
          </Avatar>

          <div className="hidden md:flex flex-col items-start">
            <span className="text-base font-medium text-gray-400">
              {user?.name}
            </span>
          </div>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="text-gray-400">
        <DropdownMenuLabel>
          <div className="flex relative items-center gap-3 py-2">
            <Avatar className="h-10 w-10 ">
              {/* Remove or comment out AvatarImage to see the fallback */}
              {/* <AvatarImage src="https://github.com/shadcn.png" /> */}
              <AvatarFallback className="bg-yellow-500 text-yellow-900 text-sm font-bold">
                {user?.name?.[0]?.toUpperCase() || "U"}
              </AvatarFallback>
            </Avatar>

            <div className="flex flex-col">
              <span className="text-base font-medium text-gray-400">
                {user?.name}
              </span>
              <span className="text-sm text-gray-500"> {user?.email} </span>
            </div>
          </div>
        </DropdownMenuLabel>
        <DropdownMenuSeparator className="bg-gray-600" />
        <DropdownMenuItem
          onClick={handleSignOut}
          className="text-gray-100 font-medium focus:bg-transparent focus:text-yellow-500 transition-colors cursor-pointer"
        >
          <LogOut className="h-4 w-4 mr-2 hidden sm:block" />
          Logout
        </DropdownMenuItem>
        <DropdownMenuSeparator className="hidden sm:block bg-gray-600" />
        <nav className="sm:hidden">
          <NavItems />
        </nav>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}
