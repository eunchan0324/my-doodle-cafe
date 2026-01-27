// src/components/admin/AdminTable.tsx
import type { ReactNode } from 'react';

interface AdminTableProps {
  headers: string[];
  children: ReactNode;
}

export default function AdminTable({ headers, children }: AdminTableProps) {
  return (
    <div className="overflow-hidden rounded-xl border-2 border-ink">
      <table className="w-full border-collapse text-left text-sm">
        <thead className="bg-crayon text-white">
          <tr className="font-doodle">
            {headers.map((header) => (
              <th key={header} className="border-b-2 border-ink px-4 py-3">
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="font-sans">{children}</tbody>
      </table>
    </div>
  );
}
